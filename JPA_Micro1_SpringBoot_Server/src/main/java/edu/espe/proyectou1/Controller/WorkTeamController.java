package edu.espe.proyectou1.Controller;

import edu.espe.proyectou1.Model.WorkTeam;
import edu.espe.proyectou1.Service.WorkTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/m1/workteams")
public class WorkTeamController {

    @Autowired
    private WorkTeamService workTeamService;

    @GetMapping("/list")
    public List<WorkTeam> findAll() {
        return workTeamService.findAll();
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody WorkTeam workTeam) {
        return workTeamService.save(workTeam);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        return workTeamService.deleteById(id);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        return workTeamService.findById(id);
    }

    @PutMapping("/updateById/{id}")
    public ResponseEntity<?> updateById(@PathVariable String id, @RequestBody WorkTeam workTeam) {
        return workTeamService.updateById(id, workTeam);
    }
}