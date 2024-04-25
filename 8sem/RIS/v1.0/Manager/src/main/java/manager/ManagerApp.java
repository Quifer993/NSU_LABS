package manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static manager.util.Constants.URL;

@SpringBootApplication

public class ManagerApp {
    public static void main(String[] args) {
        URL = args[0];
        SpringApplication.run(ManagerApp.class, args);
    }
}
