package manager;

import manager.config.RabbitConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import static manager.util.Constants.URL;
import static manager.util.Constants.URL_MONGO;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
@Import(RabbitConfiguration.class)
public class ManagerApp {
    public static void main(String[] args) {
        URL = args[0];
//        URL_MONGO = args[1];
        SpringApplication.run(ManagerApp.class, args);
    }
}
