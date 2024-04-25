package manager.util;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import manager.models.JsonBodies.ResultBody;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static manager.util.Constants.*;

@Deprecated
@Log4j2
@AllArgsConstructor
public class TaskWorkerThread implements Runnable {
    String uuid;
    RestTemplate restTemplate;
    String json;
    Map<String, ResultBody> mapTasks;

    //static final String urlTask = "http://localhost:8080/internal/api/worker/hash/crack/task";
    static final String urlTask = "http://worker:8080/internal/api/worker/hash/crack/task";
    //через переменные окружения

    public void run(){
        try{
            String[] arrAnswer = restTemplate.postForEntity(
                    urlTask,
                    json,
                    String[].class).getBody();
        }catch(Exception e){
            log.error(e.getMessage());
        }

    }
}
