package manager.controller;

import com.rabbitmq.client.Channel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import manager.models.JsonBodies.ResultData;
import manager.models.ManagerModel;
import manager.models.JsonBodies.RequestHashCodeBody;
import manager.models.JsonBodies.ResultBody;
import manager.repositories.TasksRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

import static manager.util.Constants.QUEUE_TO_MANAGER;


@RestController
@RequestMapping("/api/hash")
@Log4j2
public class ManagerController {
    @Autowired
    ManagerModel managerModel;

    @PostMapping("/crack")
    public String checkHash(@RequestBody RequestHashCodeBody reqBody){
        return managerModel.send(
                reqBody
        );
    }

    @GetMapping("/status")
    public ResultData getStatus(@RequestParam String requestId){
        return managerModel.get(requestId);
    }

    @RabbitListener(queues = QUEUE_TO_MANAGER)
    public void getResult(@RequestParam String resultString, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        log.info("Incoming message about task from worker");
        managerModel.parseAnswer(resultString, channel, tag);

    }
}
