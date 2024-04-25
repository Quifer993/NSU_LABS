package zolotorevskii.risLab.worker.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;
import zolotorevskii.risLab.worker.models.WorkerModel;

import static zolotorevskii.risLab.worker.utils.Constants.QUEUE_TO_WORKER;

@Log4j2
@RestController
@AllArgsConstructor
public class WorkerController {
    WorkerModel workerModel;

//    @PostMapping("/status")
//    @RabbitListener(queues = "queue1")
//    public ResultBody checkHash(@RequestBody String uuid) {
//        log.info("workerModel.getInfo({uuid = " + uuid + "}) - executing");
//        return workerModel.getInfo(uuid);
//    }

    @RabbitListener(queues = QUEUE_TO_WORKER,
            containerFactory = "rabbitListenerContainerFactory")
    public void receiveTask(@RequestBody String jsonString, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws JsonProcessingException {
        try{
            log.info("workerModel.exeTask() - executing. json: " + jsonString);
            workerModel.exeTask(jsonString, channel, tag);
        }catch (Exception e){
            log.info("workerModel.exeTask() - stop. error: " + e.getMessage());
            workerModel.nack(channel, tag);
        }

    }

}
