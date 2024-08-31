package edu.espe.proyectou1.Controller;

import edu.espe.proyectou1.Model.Rol;
import edu.espe.proyectou1.Service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/m1/rol")
public class RolController {
    @Autowired
    private RolService rolService;

    @PostMapping(value = "/save")
    public ResponseEntity<?> save(@RequestBody Rol rol) {
        return rolService.save(rol);
    }

    @GetMapping(value = "/listRoles")
    public List<Rol> listRoles() {
        return rolService.findAll();
    }

    @GetMapping(value = "/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        return rolService.findById(id);
    }

    @DeleteMapping(value = "/deleteById/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        return rolService.deleteById(id);
    }

    @PutMapping(value = "/updateById/{id}")
    public ResponseEntity<?> updateById(@PathVariable String id, @RequestBody Rol rol) {
        return rolService.updateById(id, rol);
    }
}
