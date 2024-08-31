package edu.espe.proyectou1.Controller.rabbit;

import edu.espe.proyectou1.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Consumer {
    @Autowired
    private UserService userService;

    @RabbitListener(queues = "queue.A")
    private void receiveFromA(String id){
        userService.findById(id);
        log.info("Respuesta cola A");
    }
}
