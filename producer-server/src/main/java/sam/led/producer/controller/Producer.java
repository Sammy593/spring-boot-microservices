package sam.led.producer.controller;


import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class Producer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private DirectExchange exchange;

    @GetMapping("/mqp/m1/user/findById/{id}")
    public String send1(@PathVariable String id){
        rabbitTemplate.convertAndSend(exchange.getName(), "routing.A", id);
        return "Message send successfully";
    }

    @GetMapping("/mqp/m2/project/countProjectsByUserId/{id}")
    public String send2(@PathVariable String id){
        rabbitTemplate.convertAndSend(exchange.getName(), "routing.B", id);
        return "Message send successfully";
    }

    @GetMapping("/mqp/m2/task/countTasksByUserId/{id}")
    public String send3(@PathVariable String id){
        rabbitTemplate.convertAndSend(exchange.getName(), "routing.C", id);
        return "Message send successfully";
    }

}
