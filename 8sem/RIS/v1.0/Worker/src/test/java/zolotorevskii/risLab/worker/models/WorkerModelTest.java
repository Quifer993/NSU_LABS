package zolotorevskii.risLab.worker.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import zolotorevskii.risLab.worker.models.jsonBodies.CrackHashManagerRequest;
import zolotorevskii.risLab.worker.models.jsonBodies.ResultBody;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static zolotorevskii.risLab.worker.utils.Constants.IN_PROGRESS;
import static zolotorevskii.risLab.worker.utils.Constants.READY;

class WorkerModelTest {
    WorkerModel workerModel = new WorkerModel();
    ObjectMapper objectMapper = new ObjectMapper();
    private final String alphabet = "abcdefghij";

    @Test
    void testParseHashCode() throws JsonProcessingException {

        var inputHashTest = new CrackHashManagerRequest("0", 1, 1,
                "e2fc714c4727ee9395f324cd2e7f331f",
                6,
                alphabet);
        workerModel.exeTask(objectMapper.writeValueAsString(inputHashTest));
        var listTest = new ArrayList<String>();
        listTest.add("abcd");
        while(workerModel.getInfo("0").getStatus().equals(IN_PROGRESS)){

        }

        Assertions.assertEquals(workerModel.getInfo("0").getData(), listTest);
        Assertions.assertEquals(workerModel.getInfo("0").getStatus(),READY);
    }

}