package zolotorevskii.risLab.worker.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.paukov.combinatorics3.Generator;
import org.springframework.web.bind.annotation.RequestBody;
import zolotorevskii.risLab.worker.models.jsonBodies.CrackHashManagerRequest;
import zolotorevskii.risLab.worker.models.jsonBodies.ResultBody;
import zolotorevskii.risLab.worker.utils.ParseMD5Code;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static zolotorevskii.risLab.worker.utils.Constants.*;

public class WorkerModel{
    private final ObjectMapper mapper = new ObjectMapper();
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
    private final Map<String, TaskProperties> mapTasks = new ConcurrentHashMap<>();

    public void exeTask(@RequestBody String jsonString) throws JsonProcessingException {
        CrackHashManagerRequest inputHash = mapper.readValue(jsonString, CrackHashManagerRequest.class);
        String[] alphabet = inputHash.getAlphabet().split("");

        TaskProperties taskProperties = new TaskProperties(
                java.time.LocalDateTime.now(),
                new ArrayList<String>(),
                inputHash.getHash(),
                inputHash,
                false,
                false
        );

        mapTasks.put(inputHash.getGuid(), taskProperties);
        TaskRunnable taskRunnable = new TaskRunnable(inputHash);
        executorService.execute(taskRunnable);
    }

    public ResultBody getInfo(String uuid){
        TaskProperties taskProperties = mapTasks.get(uuid);
        if(taskProperties == null){
            return null;
        }
        if(taskProperties.isError()){
            return new ResultBody(ERROR, null);
        }
        if(taskProperties.isDone()){
            return new ResultBody(READY, taskProperties.getAnswers());
        }
        if(Duration.between(taskProperties.getStartTime(), java.time.LocalDateTime.now()).toSeconds() > TIMEOUT){
            taskProperties.setError(true);
            return new ResultBody(ERROR, null);
        }
        return new ResultBody(IN_PROGRESS, null);
    }

    @AllArgsConstructor
    class TaskRunnable implements  Runnable {
        CrackHashManagerRequest inputHash;

        @Override
        public void run() {
            var answer = new ArrayList<String>();
            for (int i = 1; i <= inputHash.getMaxLength(); i++){
                var alphabetInSplit = inputHash.getAlphabet().split("");
                Generator.combination(alphabetInSplit)
                        .simple(i)
                        .stream()
                        .filter(x -> ParseMD5Code.parse(ParseMD5Code.listCollapse(x)).equals(inputHash.getHash()))
                        .toList()
                        .forEach(x -> answer.add(ParseMD5Code.listCollapse(x)));
            }

            TaskProperties taskProperties = mapTasks.get(inputHash.getGuid());
            var f = Duration.between(java.time.LocalDateTime.now(), taskProperties.getStartTime()).toSeconds();
            if(Duration.between(taskProperties.getStartTime(), java.time.LocalDateTime.now()).toSeconds() > TIMEOUT) {
                taskProperties.setError(true);
            } else {
                taskProperties.setAnswers(answer);
                taskProperties.setDone(true);
                mapTasks.put(inputHash.getGuid(), taskProperties);
            }
        }
    }
}
