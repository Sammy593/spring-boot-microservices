package edu.espe.discoveryserveru2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServerU2Application {

    public static void main(String[] args) {
        SpringApplication.run(DiscoveryServerU2Application.class, args);
    }

}
