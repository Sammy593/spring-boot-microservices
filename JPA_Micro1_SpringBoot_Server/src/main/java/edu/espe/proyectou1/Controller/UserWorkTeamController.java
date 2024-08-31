package edu.espe.proyectou1.Controller;

import edu.espe.proyectou1.Dto.UserWorkTeamDTO;
import edu.espe.proyectou1.Model.UserWorkTeam;
import edu.espe.proyectou1.Service.UserWorkTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/m1/userworkteams")
public class UserWorkTeamController {

    @Autowired
    private UserWorkTeamService userWorkTeamService;

    @GetMapping("/list")
    public List<UserWorkTeam> findAll() {
        return userWorkTeamService.findAll();
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody UserWorkTeamDTO userWorkTeamDTO) {
        return userWorkTeamService.save(userWorkTeamDTO);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        return userWorkTeamService.deleteById(id);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        return userWorkTeamService.findById(id);
    }

    @PutMapping("/updateById/{id}")
    public ResponseEntity<?> updateById(@PathVariable String id, @RequestBody UserWorkTeamDTO userWorkTeamDTO) {
        return userWorkTeamService.updateById(id, userWorkTeamDTO);
    }
}