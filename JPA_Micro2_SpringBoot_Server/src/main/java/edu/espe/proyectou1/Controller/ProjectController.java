package edu.espe.proyectou1.Controller;

import edu.espe.proyectou1.Dto.ProjectDTO;
import edu.espe.proyectou1.Model.Project;
import edu.espe.proyectou1.Service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/m2/project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @PostMapping(value = "/save")
    public ResponseEntity<?> save(@RequestBody ProjectDTO projectDTO) {
        return projectService.save(projectDTO);
    }

    @GetMapping(value = "/listProjects")
    public List<ProjectDTO> listProjects() {
        return projectService.findAll();
    }

    @GetMapping(value = "/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        return projectService.findById(id);
    }

    @DeleteMapping(value = "/deleteById/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        return projectService.deleteById(id);
    }

    @PutMapping(value = "/updateById/{id}")
    public ResponseEntity<?> updateById(@PathVariable String id, @RequestBody ProjectDTO projectDTO) {
        return projectService.updateById(id, projectDTO);
    }

    @GetMapping(value = "/countProjectsByUserId/{userId}")
    public ResponseEntity<?> countProjectsByUserId(@PathVariable String userId) {
        return projectService.countProjectsByUserId(userId);
    }
}
