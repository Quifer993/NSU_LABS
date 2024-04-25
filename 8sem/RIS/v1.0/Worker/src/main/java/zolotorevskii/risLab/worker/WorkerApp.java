package zolotorevskii.risLab.worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zolotorevskii.risLab.worker.utils.Constants;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication

public class WorkerApp {
    public static void main(String[] args) {
        Constants.URL = args[0];
        SpringApplication.run(WorkerApp.class, args);
    }
}