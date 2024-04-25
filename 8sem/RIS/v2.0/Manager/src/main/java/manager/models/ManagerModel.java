package manager.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import manager.models.JsonBodies.CrackHashManagerRequest;
import manager.models.JsonBodies.RequestHashCodeBody;
import manager.models.JsonBodies.ResultBody;
import manager.models.JsonBodies.ResultData;
import manager.repositories.TasksNotReadyRepository;
import manager.repositories.TasksRepository;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static manager.util.Constants.*;

@Component
@Log4j2
public class ManagerModel implements ConnectionListener {
    ObjectMapper objectMapper = new ObjectMapper();
    private final String alphabet = "abcdefghijklmnopqrstuvwxyz0123456789";

    @Autowired
    TasksRepository tasksRepository;
    @Autowired
    TasksNotReadyRepository tasksNotReadyRepository;
    AmqpTemplate template = rabbitTemplate();

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory =
                new CachingConnectionFactory(URL);
        connectionFactory.addConnectionListener(this);
        return connectionFactory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

    @Override
    public void onCreate(@NonNull Connection connection) {
        log.info("Connection to RabbitMQ created");
        log.info("tasks before " + tasksNotReadyRepository.findAll());

        try{
            tasksNotReadyRepository.findAll().forEach(x -> {
                try {
                    log.info("send " + x);
                    template.convertAndSend(QUEUE_TO_WORKER, objectMapper.writeValueAsString(x));
                    tasksNotReadyRepository.deleteById(x.getGuid());
                } catch (JsonProcessingException e) {
                    log.error("Can`t parse object into string json: " + x + ". OR Rabbit not work");
                }
            });
            log.info("Finish send unsent messages");
            log.info("tasks after " + tasksNotReadyRepository.findAll());
        }catch (Exception e){
            log.error("rabbitMQ or database off");
        }
    }

    public ResultData get(String uuid){
        try {
            var optionalResult = tasksRepository.findById(uuid);
            return optionalResult.orElse(null);
        }catch (Exception e){
            log.error("Can`t take task of uuid - " + uuid + " : " + e.getMessage());
            return null;
        }
//        return restTemplate.postForEntity(
//                urlGetStatus,
//                uuid,
//                ResultBody.class).getBody();
    }


    /**Отправляет задачу к Worker-у с помощью RabbitMQ, сохраняя её в Mongodb
     * @param reqBody Начальные значения обрабатываемого hash и его максимальная длина maxLength
     * @return Номер задачи
     */
    public String send(RequestHashCodeBody reqBody) {
        String uuid = UUID.randomUUID().toString();
        ConcurrentHashMap<Integer, String> map = new ConcurrentHashMap<>();
        for(int i = 1; i <= COUNT_WORKER; i++){
            map.put(i, IN_PROGRESS);
        }

        ResultData task = new ResultData(uuid, new CopyOnWriteArrayList<>(), map);

        for(int i = 1; i <= COUNT_WORKER; i++){
            CrackHashManagerRequest bodyJson = new CrackHashManagerRequest(
                    uuid,
                    i,
                    COUNT_WORKER,
                    reqBody.getHash(),
                    reqBody.getMaxLength(),
                    alphabet
            );

            String json;
            try {
                json = objectMapper.writeValueAsString(bodyJson);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                return null;
            }

            try{
                tasksNotReadyRepository.insert(bodyJson);
            }catch (Exception e){
                log.error("Can`t insert part of task into database: {error} : " + e.getMessage());
                return "Can`t insert part of task into database: {error} : " + e.getMessage();
            }

            try{
                template.convertAndSend(QUEUE_TO_WORKER, json);
                tasksNotReadyRepository.deleteById(uuid);
                log.info("task was delivered in queue");
            }catch(AmqpException e) {
                log.error("task wasn't delivered in queue: " + e.getMessage());
            }catch (Exception e){
                log.error("Can`t delete part of task into database: {error} : " + e.getMessage());
            }
        }
        try{
            tasksRepository.insert(task);
        }catch (Exception e){
            log.error("Can`t insert task into database: {error} : " + e.getMessage());
            return "Can`t insert task into database: {error} : " + e.getMessage();
        }
        return uuid;
    }

    public void parseAnswer(String resultString, Channel channel, long tag) {
        ResultBody resultBody;
        try {
            log.info("read result in mongodb");
            resultBody = objectMapper.readValue(resultString, ResultBody.class);
            log.info("save result in mongodb");
            var task = tasksRepository.findById(resultBody.getUuid()).orElse(null);
            if(task == null){
                throw new Exception();
            }
            resultBody.getData().forEach(x -> {
                        if(!task.getData().contains(x)){
                            task.getData().add(x);
                        }
            }
            );
            task.getStatuses().put(resultBody.getPartNumber(), READY);
            tasksRepository.save(task);
            log.info("save success");
            channel.basicAck(tag, false);
        } catch (JsonProcessingException e) {
            log.error("incorrect info from worker : " + resultString);
            nack(channel, tag);
        }catch (Exception e){
            log.error("Can`t save result in mongodb: " + e.getMessage());
            nack(channel, tag);
        }
    }

    private void nack(Channel channel, long tag) {
        try{
            channel.basicNack(tag, false, true);
        } catch (IOException ex) {
            log.error("Can`t Nack result in RabbitMQ: " + ex.getMessage());
        }
    }
}
