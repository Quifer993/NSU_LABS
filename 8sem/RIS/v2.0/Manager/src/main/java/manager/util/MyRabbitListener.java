package manager.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import manager.repositories.TasksNotReadyRepository;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static manager.util.Constants.QUEUE_TO_WORKER;

@Deprecated
@Log4j2
@Component
@NoArgsConstructor
public class MyRabbitListener implements ConnectionListener {
    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    TasksNotReadyRepository tasksNotReadyRepository;
    @Autowired
    AmqpTemplate template;
    @Override
    public void onCreate(@NonNull Connection connection) {
        log.info("Connection to RabbitMQ created");
        log.info("tasks before " + tasksNotReadyRepository.findAll());

        try{
            tasksNotReadyRepository.findAll().forEach(x -> {
                try {
                    template.convertAndSend(QUEUE_TO_WORKER, objectMapper.writeValueAsString(x));
                    tasksNotReadyRepository.delete(x);
                } catch (JsonProcessingException e) {
                    log.error("Can`t parse object into string json: " + x);
                }
            });
            log.info("tasks after " + tasksNotReadyRepository.findAll());
        }catch (Exception e){
            log.error("rabbitMQ disconnect again// or database off");
        }

    }

    @Override
    public void onClose(Connection connection) {
        ConnectionListener.super.onClose(connection);
        log.info("rabbit closed");
    }
}