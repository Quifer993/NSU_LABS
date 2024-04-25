package zolotorevskii.risLab.worker.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.paukov.combinatorics.CombinatoricsFactory;
import org.paukov.combinatorics.ICombinatoricsVector;
import org.paukov.combinatorics3.Generator;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import zolotorevskii.risLab.worker.models.jsonBodies.CrackHashManagerRequest;
import zolotorevskii.risLab.worker.models.jsonBodies.ResultBody;
import zolotorevskii.risLab.worker.utils.ParseMD5Code;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static zolotorevskii.risLab.worker.utils.Constants.*;

@Log4j2
@Component
public class WorkerModel{
    private final ObjectMapper mapper = new ObjectMapper();
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
    @Autowired
    AmqpTemplate template;

    public void exeTask(String jsonString, Channel channel, long tag) throws JsonProcessingException {
        CrackHashManagerRequest inputHash = mapper.readValue(jsonString, CrackHashManagerRequest.class);
        String[] alphabet = inputHash.getAlphabet().split("");

        TaskProperties taskProperties = new TaskProperties(
                channel,
                tag,
                java.time.LocalDateTime.now(),
                new ArrayList<String>(),
                inputHash.getHash(),
                inputHash,
                inputHash.getPartNumber(),
                inputHash.getPartCount()
        );

        TaskRunnable taskRunnable = new TaskRunnable(inputHash, taskProperties);
        executorService.execute(taskRunnable);
    }

    @AllArgsConstructor
    class TaskRunnable implements  Runnable {
        CrackHashManagerRequest inputHash;
        TaskProperties taskProperties;

        @Override
        public void run() {
            try {
                var answer = new ArrayList<String>();

                var alphabetInSplit = inputHash.getAlphabet().split("");
                int alphabetSize = alphabetInSplit.length;
                int partNumber = inputHash.getPartNumber();
                int partCount = inputHash.getPartCount();
                String[] partAlp = Arrays.copyOfRange(alphabetInSplit,
                        alphabetSize * (partNumber-1)/ partCount,
                        alphabetSize * partNumber / partCount);

                for (int i = 1; i <= inputHash.getMaxLength(); i++){
                    Generator.combination(partAlp)
                            .simple(i)
                            .stream()
                            .filter(x -> ParseMD5Code.parse(ParseMD5Code.listCollapse(x)).equals(inputHash.getHash()))
                            .toList()
                            .forEach(x -> answer.add(ParseMD5Code.listCollapse(x)));
                }

                ResultBody resultBody;
                if(Duration.between(taskProperties.getStartTime(), java.time.LocalDateTime.now()).toSeconds() > TIMEOUT) {
                    resultBody = new ResultBody(inputHash.getGuid(), ERROR, null, partNumber);
                } else {
                    resultBody = new ResultBody(inputHash.getGuid(), READY, answer, partNumber);
                }

                String json = mapper.writeValueAsString(resultBody);
                log.info("Task " + resultBody.getUuid() + " completed. resultBody - " + resultBody);
                template.convertAndSend(QUEUE_TO_MANAGER, json);
                log.info("Data was delivered to queue " + QUEUE_TO_MANAGER);
                taskProperties.getChannel().basicAck(taskProperties.getTag(), false);
            } catch (IOException e) {
                log.error(e.getMessage());
                nack(taskProperties.getChannel(), taskProperties.getTag());
            }
        }
    }

        public void nack(Channel channel, long tag) {
            try{
                channel.basicNack(tag, false, true);
            } catch (IOException ex) {
                log.error("Can`t Nack result in RabbitMQ: " + ex.getMessage());
            }
        }
    }

