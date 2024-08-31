package edu.espe.proyectou1.Service;

import edu.espe.proyectou1.Dto.CompanyDTO;
import edu.espe.proyectou1.Model.Company;
import edu.espe.proyectou1.Repository.CompanyRepository;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ObservationRegistry observationRegistry;

    public List<CompanyDTO> findAll() {
        return Observation.createNotStarted("company-service.findAll", observationRegistry)
                .observe(() -> companyRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()));
    }

    public ResponseEntity<?> save(CompanyDTO companyDTO) {
        return Observation.createNotStarted("company-service.save", observationRegistry)
                .observe(() -> {
                    Company company = convertToEntity(companyDTO);
                    Company savedCompany = companyRepository.save(company);
                    return new ResponseEntity<>(convertToDTO(savedCompany), HttpStatus.CREATED);
                });
    }

    public ResponseEntity<CompanyDTO> findById(String id) {
        return Observation.createNotStarted("company-service.findById", observationRegistry)
                .observe(() -> {
                    Optional<Company> companyOptional = companyRepository.findById(UUID.fromString(id));
                    return companyOptional
                            .map(company -> new ResponseEntity<>(convertToDTO(company), HttpStatus.OK))
                            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
                });
    }

    public ResponseEntity<CompanyDTO> update(CompanyDTO companyDTO) {
        return Observation.createNotStarted("company-service.update", observationRegistry)
                .observe(() -> {
                    if (companyRepository.existsById(companyDTO.getId())) {
                        Company company = convertToEntity(companyDTO);
                        Company updatedCompany = companyRepository.save(company);
                        return new ResponseEntity<>(convertToDTO(updatedCompany), HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                    }
                });
    }

    public ResponseEntity<Void> deleteById(String id) {
        return Observation.createNotStarted("company-service.deleteById", observationRegistry)
                .observe(() -> {
                    if (companyRepository.existsById(UUID.fromString(id))) {
                        companyRepository.deleteById(UUID.fromString(id));
                        return new ResponseEntity<>(HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                    }
                });
    }


    private CompanyDTO convertToDTO(Company company) {
        CompanyDTO dto = new CompanyDTO();
        dto.setId(company.getId());
        dto.setName(company.getName());
        dto.setDescription(company.getDescription());

        return dto;
    }

    private Company convertToEntity(CompanyDTO dto) {
        Company company = new Company();
        company.setId(dto.getId());
        company.setName(dto.getName());
        company.setDescription(dto.getDescription());
        return company;
    }
}