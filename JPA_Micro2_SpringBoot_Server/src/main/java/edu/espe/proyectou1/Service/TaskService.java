package edu.espe.proyectou1.Service;

import edu.espe.proyectou1.Dto.TaskDTO;
import edu.espe.proyectou1.Model.Project;
import edu.espe.proyectou1.Model.Task;
import edu.espe.proyectou1.Payload.response.TaskWithResponsible;
import edu.espe.proyectou1.Repository.ProjectRepository;
import edu.espe.proyectou1.Repository.TaskRepository;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private ObservationRegistry observationRegistry;

    public List<TaskDTO> findAll() {
        return Observation.createNotStarted("task-service.findAll", observationRegistry)
                .observe(() ->
                     taskRepository.findAll().stream()
                            .map(this::convertToDTO)
                            .collect(Collectors.toList()));
    }

    public List<TaskDTO> findAllByProject(String projectId) {
        return Observation.createNotStarted("task-service.findAllByProject", observationRegistry)
                .observe(() -> taskRepository.findByProject_Id(UUID.fromString(projectId)).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()));
    }

    public ResponseEntity<?> save(TaskDTO taskDTO) {
        return Observation.createNotStarted("task-service.save", observationRegistry)
                .observe(() -> {
                    // Verificar que el usuario existe
                    String url = "http://localhost:8085/mqp/m1/user/findById/" + taskDTO.getResponsible();
                    ResponseEntity<?> response = restTemplate.getForEntity(url, Object.class);

                    if (response.getStatusCode().is2xxSuccessful()) {
                        Task task = convertToEntity(taskDTO);
                        if (taskDTO.getProjectId() != null) {
                            Optional<Project> projectOptional = projectRepository.findById(taskDTO.getProjectId());
                            if (projectOptional.isPresent()) {
                                task.setProject(projectOptional.get());
                            } else {
                                return new ResponseEntity<>("Project not found", HttpStatus.NOT_FOUND);
                            }
                        }
                        Task savedTask = taskRepository.save(task);
                        return new ResponseEntity<>(convertToDTO(savedTask), HttpStatus.CREATED);
                    } else {
                        return new ResponseEntity<>("Responsible user not found", HttpStatus.NOT_FOUND);
                    }
                });
    }

    public ResponseEntity<?> deleteById(String id) {
        return Observation.createNotStarted("task-service.deleteById", observationRegistry)
                .observe(() -> {
                    if (taskRepository.existsById(UUID.fromString(id))) {
                        taskRepository.deleteById(UUID.fromString(id));
                        return new ResponseEntity<>(HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("Task not found", HttpStatus.NOT_FOUND);
                    }
                });
    }

    public ResponseEntity<?> findById(String id) {
        return Observation.createNotStarted("task-service.findById", observationRegistry)
                .observe(() -> {
                    Optional<Task> taskOptional = taskRepository.findById(UUID.fromString(id));
                    if (taskOptional.isPresent()) {
                        TaskDTO taskDTO = convertToDTO(taskOptional.get());
                        String responsibleId = taskDTO.getResponsible();
                        String url = "http://localhost:8085/mqp/m1/user/findById/" + responsibleId;
                        ResponseEntity<?> response = restTemplate.getForEntity(url, Object.class);

                        if (response.getStatusCode().is2xxSuccessful()) {
                            Object userResponsible = response.getBody();
                            TaskWithResponsible taskWithResponsible = new TaskWithResponsible();
                            taskWithResponsible.setTask(taskDTO);
                            taskWithResponsible.setResponsibleObject(userResponsible);

                            return new ResponseEntity<>(taskWithResponsible, HttpStatus.OK);
                        } else {
                            return new ResponseEntity<>("Responsible user not found", HttpStatus.NOT_FOUND);
                        }
                    } else {
                        return new ResponseEntity<>("Task not found", HttpStatus.NOT_FOUND);
                    }
                });
    }

    public ResponseEntity<?> updateById(String id, TaskDTO taskDTO) {
        return Observation.createNotStarted("task-service.updateById", observationRegistry)
                .observe(() -> {
                    if (taskRepository.existsById(UUID.fromString(id))) {
                        Task task = convertToEntity(taskDTO);
                        task.setId(UUID.fromString(id));
                        if (taskDTO.getProjectId() != null) {
                            Optional<Project> projectOptional = projectRepository.findById(taskDTO.getProjectId());
                            if (projectOptional.isPresent()) {
                                task.setProject(projectOptional.get());
                            } else {
                                return new ResponseEntity<>("Project not found", HttpStatus.NOT_FOUND);
                            }
                        }
                        Task updatedTask = taskRepository.save(task);
                        return new ResponseEntity<>(convertToDTO(updatedTask), HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("Task not found", HttpStatus.NOT_FOUND);
                    }
                });
    }

    public ResponseEntity<?> countTasksByUserId(String userId) {
        return Observation.createNotStarted("task-service.countTasksByUserId", observationRegistry)
                .observe(() -> {
                    List<Task> tasks = taskRepository.findByResponsible(userId);
                    return new ResponseEntity<>(tasks.size(), HttpStatus.OK);
                });
    }

    private TaskDTO convertToDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setName(task.getName());
        dto.setDescription(task.getDescription());
        dto.setStartDate(task.getStartDate());
        dto.setEndDate(task.getEndDate());
        dto.setResponsible(task.getResponsible());
        dto.setState(task.getState());
        if (task.getProject() != null) {
            dto.setProjectId(task.getProject().getId());
        }
        return dto;
    }

    private Task convertToEntity(TaskDTO dto) {
        Task task = new Task();
        task.setId(dto.getId());
        task.setName(dto.getName());
        task.setDescription(dto.getDescription());
        task.setStartDate(dto.getStartDate());
        task.setEndDate(dto.getEndDate());
        task.setResponsible(dto.getResponsible());
        task.setState(dto.getState());
        // No establecemos el proyecto aquí, se maneja por separado
        return task;
    }
}

