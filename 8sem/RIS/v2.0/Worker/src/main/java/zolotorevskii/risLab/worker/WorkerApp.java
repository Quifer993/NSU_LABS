package zolotorevskii.risLab.worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import zolotorevskii.risLab.worker.configuration.RabbitConfiguration;
import zolotorevskii.risLab.worker.utils.Constants;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
@Import(RabbitConfiguration.class)
public class WorkerApp {
    public static void main(String[] args) {
        Constants.URL = args[0];
        SpringApplication.run(WorkerApp.class, args);
    }
}