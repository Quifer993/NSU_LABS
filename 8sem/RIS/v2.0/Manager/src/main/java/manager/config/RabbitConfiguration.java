package manager.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static manager.util.Constants.QUEUE_TO_MANAGER;
import static manager.util.Constants.QUEUE_TO_WORKER;

@Log4j2
@Configuration
public class RabbitConfiguration {
    @Bean
    public Queue myQueue1() {
        return new Queue(QUEUE_TO_WORKER);
    }
    @Bean
    public Queue queueToManager() {
        return new Queue(QUEUE_TO_MANAGER);
    }
}