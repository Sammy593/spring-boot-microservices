package edu.espe.proyectou1.Service;

import edu.espe.proyectou1.Dto.ProjectDTO;
import edu.espe.proyectou1.Model.Company;
import edu.espe.proyectou1.Model.Project;
import edu.espe.proyectou1.Model.Task;
import edu.espe.proyectou1.Payload.response.ProjectWithLeader;
import edu.espe.proyectou1.Repository.CompanyRepository;
import edu.espe.proyectou1.Repository.ProjectRepository;
import edu.espe.proyectou1.Repository.TaskRepository;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CompanyRepository companyRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private ObservationRegistry observationRegistry;

    public List<ProjectDTO> findAll() {
        return Observation.createNotStarted("project-service.findAll", observationRegistry)
                .observe(() -> projectRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()));
    }

    public ResponseEntity<?> save(ProjectDTO projectDTO) {
        return Observation.createNotStarted("project-service.save", observationRegistry)
                .observe(() -> {
                    // Verificar que el usuario existe
                    String url = "http://localhost:8085/mqp/m1/user/findById/" + projectDTO.getIdLeader();
                    ResponseEntity<?> response = restTemplate.getForEntity(url, Object.class);

                    if (response.getStatusCode().is2xxSuccessful()) {
                        Project project = convertToEntity(projectDTO);

                        // Verificar y asignar la empresa si se proporciona un companyId
                        if (projectDTO.getCompanyId() != null) {
                            Optional<Company> companyOptional = companyRepository.findById(projectDTO.getCompanyId());
                            if (companyOptional.isPresent()) {
                                project.setCompany(companyOptional.get());
                            } else {
                                return new ResponseEntity<>("Company not found", HttpStatus.NOT_FOUND);
                            }
                        }

                        Project savedProject = projectRepository.save(project);
                        return new ResponseEntity<>(convertToDTO(savedProject), HttpStatus.CREATED);
                    } else {
                        return new ResponseEntity<>("Leader user not found", HttpStatus.NOT_FOUND);
                    }
                });
    }

    public ResponseEntity<?> deleteById(String id) {
        return Observation.createNotStarted("project-service.deleteById", observationRegistry)
                .observe(() -> {
                    if (projectRepository.existsById(UUID.fromString(id))) {
                        projectRepository.deleteById(UUID.fromString(id));
                        return new ResponseEntity<>(HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("Project not found", HttpStatus.NOT_FOUND);
                    }
                });
    }

    public ResponseEntity<?> findById(String id) {
        return Observation.createNotStarted("project-service.findbyId", observationRegistry)
                .observe(() -> {
                    Optional<Project> projectOptional = projectRepository.findById(UUID.fromString(id));
                    if (projectOptional.isPresent()) {
                        ProjectDTO projectDTO = convertToDTO(projectOptional.get());
                        String leaderId = projectDTO.getIdLeader();

                        ProjectWithLeader projectResponse = new ProjectWithLeader();
                        projectResponse.setProject(projectDTO);

                        try {
                            String url = "http://localhost:8085/mqp/m1/user/findById/" + leaderId;
                            ResponseEntity<?> response = restTemplate.getForEntity(url, Object.class);

                            if (response.getStatusCode().is2xxSuccessful()) {
                                Object userLeader = response.getBody();
                                projectResponse.setUserLeader(userLeader);
                            } else {
                                // Si no se encuentra el usuario, simplemente dejamos el userLeader como null
                                projectResponse.setUserLeader(null);
                            }
                        } catch (HttpClientErrorException.NotFound e) {
                            // Si no se encuentra el usuario, simplemente dejamos el userLeader como null
                            projectResponse.setUserLeader(null);
                        } catch (Exception e) {
                            // Loguear el error pero no fallar la respuesta
                            System.err.println("Error al obtener el líder del proyecto: " + e.getMessage());
                        }

                        return new ResponseEntity<>(projectResponse, HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("Project not found", HttpStatus.NOT_FOUND);
                    }
                });
    }

    public ResponseEntity<?> updateById(String id, ProjectDTO projectDTO) {
        return Observation.createNotStarted("project-service.updateById", observationRegistry)
                .observe(() -> {
                    if (projectRepository.existsById(UUID.fromString(id))) {
                        Project project = convertToEntity(projectDTO);
                        project.setId(UUID.fromString(id));
                        Project updatedProject = projectRepository.save(project);
                        return new ResponseEntity<>(convertToDTO(updatedProject), HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("Project not found", HttpStatus.NOT_FOUND);
                    }
                });
    }

    public Boolean updateProjectProgress(String projectId) {
        return Observation.createNotStarted("project-service.updateProjectProgress", observationRegistry)
                .observe(() -> {
                    Optional<Project> projectOptional = projectRepository.findById(UUID.fromString(projectId));

                    if (projectOptional.isPresent()) {
                        Project project = projectOptional.get();
                        List<Task> tasks = taskRepository.findByProject_Id(UUID.fromString(projectId));

                        long completedTasks = tasks.stream().filter(task -> "Listo".equals(task.getState())).count();
                        double progress = tasks.size() > 0 ? (double) completedTasks / tasks.size() * 100 : 0.0;

                        project.setProgress(progress);
                        projectRepository.save(project);
                        return true;
                    }
                    return false;
                });
    }

    public ResponseEntity<?> countProjectsByUserId(String userId) {
        return Observation.createNotStarted("project-service.countProjectsByUserId", observationRegistry)
                .observe(() -> {
                    List<Project> projects = projectRepository.findByIdLeader(userId);
                    return new ResponseEntity<>(projects.size(), HttpStatus.OK);
                });
    }


    private ProjectDTO convertToDTO(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        dto.setProgress(project.getProgress());
        dto.setState(project.getState());
        dto.setIdLeader(project.getIdLeader());
        // Manejo seguro de la lista de tareas
        dto.setTaskIds(project.getTasks() != null
                ? project.getTasks().stream()
                .map(Task::getId)
                .collect(Collectors.toList())
                : Collections.emptyList());
        dto.setCompanyId(project.getCompany() != null ? project.getCompany().getId() : null);

        return dto;
    }

    private Project convertToEntity(ProjectDTO dto) {
        Project project = new Project();
        project.setId(dto.getId());
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        project.setProgress(dto.getProgress());
        project.setState(dto.getState());
        project.setIdLeader(dto.getIdLeader());
        // No establecemos las tareas aquí, ya que se manejan por separado
        if (dto.getCompanyId() != null) {
            Company company = new Company();
            company.setId(dto.getCompanyId());
            project.setCompany(company);
        }
        return project;
    }
}
