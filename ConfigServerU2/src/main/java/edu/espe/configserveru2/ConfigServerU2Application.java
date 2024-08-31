package edu.espe.configserveru2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerU2Application {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerU2Application.class, args);
    }

}
