package zolotorevskii.risLab.worker.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.*;
import zolotorevskii.risLab.worker.models.WorkerModel;
import zolotorevskii.risLab.worker.models.jsonBodies.ResultBody;


@RestController
@RequestMapping("/internal/api/worker/hash/crack")
public class WorkerController {
WorkerModel workerModel = new WorkerModel();

    @PostMapping("/status")
    public ResultBody checkHash(@RequestBody String uuid) {
        return workerModel.getInfo(uuid);
    }

    @PostMapping("/task")
    public void receiveTask(@RequestBody String jsonString) throws JsonProcessingException {
        workerModel.exeTask(jsonString);
    }

}
