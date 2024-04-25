package manager.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import manager.models.JsonBodies.CrackHashManagerRequest;
import manager.models.JsonBodies.RequestHashCodeBody;
import manager.models.JsonBodies.Response;
import manager.models.JsonBodies.ResultBody;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static manager.util.Constants.*;

@Component
@Log4j2
public class ManagerModel {
    ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();
    private final String alphabet = "abcdefghijklmnopqrstuvwxyz0123456789";
    static final String urlWorker = "http://" + URL + "/internal/api/worker/hash/crack/";
    static final String urlTask = urlWorker + "task";
    static final String urlGetStatus = urlWorker + "status";

    public ResultBody get(String uuid){
        return restTemplate.postForEntity(
                urlGetStatus,
                uuid,
                ResultBody.class).getBody();
    }


    public String send(RequestHashCodeBody reqBody) {
        String uuid = UUID.randomUUID().toString();

        CrackHashManagerRequest bodyJson = new CrackHashManagerRequest(
                uuid,
                1,
                1,
                reqBody.getHash(),
                reqBody.getMaxLength(),
                alphabet
        );

        String json = null;
        try {
            json = objectMapper.writeValueAsString(bodyJson);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }

        try{
            restTemplate.postForEntity(
                    urlTask,
                    json,
                    String[].class).getBody();
        }catch(Exception e){
            log.error(e.getMessage());
        }

        return uuid;
    }

    private void parseAnswer() {
//
//        mapTasks.put(uuid, new ResultBody(READY, arrAnswer));
//
//        log.error(e.getMessage());
//        mapTasks.put(uuid, new ResultBody(ERROR, null));
    }

}
