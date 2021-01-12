package am.management.management_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ManagementApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagementApiApplication.class, args);
    }

}
