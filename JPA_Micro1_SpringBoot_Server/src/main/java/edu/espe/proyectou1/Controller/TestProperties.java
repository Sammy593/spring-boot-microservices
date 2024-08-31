package edu.espe.proyectou1.Controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/m1/props/")
@Getter
@Setter
@RefreshScope
public class TestProperties {

    @Value("${app.testProp}")
    private String testProp;

    @GetMapping(value = "")
    public String list() {
        return this.testProp;
    }

}
