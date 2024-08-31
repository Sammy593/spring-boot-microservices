package edu.espe.proyectou1.Controller.rabbit;

import edu.espe.proyectou1.Service.ProjectService;
import edu.espe.proyectou1.Service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Consumer {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private TaskService taskService;

    @RabbitListener(queues = "queue.B")
    private void receiveFromB(String id){
        projectService.countProjectsByUserId(id);
        log.info("Respuesta cola B");
    }

    @RabbitListener(queues = "queue.C")
    private void receiveFromC(String id){
        taskService.countTasksByUserId(id);
        log.info("Respuesta cola C");
    }
}
