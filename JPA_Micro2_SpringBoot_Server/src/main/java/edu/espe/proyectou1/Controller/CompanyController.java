package edu.espe.proyectou1.Controller;

import edu.espe.proyectou1.Dto.CompanyDTO;
import edu.espe.proyectou1.Service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/m2/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping(value = "/list")
    public List<CompanyDTO> getAllCompanies() {
        return companyService.findAll();
    }

    @GetMapping(value = "/findById/{id}")
    public ResponseEntity<?> getCompanyById(@PathVariable String id) {
        return companyService.findById(id);
    }

    @PostMapping(value ="/save")
    public ResponseEntity<?> createCompany(@RequestBody CompanyDTO companyDTO) {
        return companyService.save(companyDTO);
    }

    @PutMapping(value = "/updateById/{id}")
    public ResponseEntity<?> updateCompany(@PathVariable String id, @RequestBody CompanyDTO companyDTO) {
        companyDTO.setId(java.util.UUID.fromString(id));
        return companyService.update(companyDTO);
    }

    @DeleteMapping(value = "/deleteById/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable String id) {
        return companyService.deleteById(id);
    }
}
