package manager.controller;

import lombok.AllArgsConstructor;
import manager.models.ManagerModel;
import manager.models.JsonBodies.RequestHashCodeBody;
import manager.models.JsonBodies.ResultBody;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/hash")
@AllArgsConstructor
public class ManagerController {
    ManagerModel managerModel;

    @PostMapping("/crack")
    public String checkHash(@RequestBody RequestHashCodeBody reqBody){
        return managerModel.send(
                reqBody
        );
    }

    @GetMapping("/status")
    public ResultBody getStatus(@RequestParam String requestId){
        return managerModel.get(requestId);
    }
}
