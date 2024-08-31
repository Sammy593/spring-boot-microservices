package edu.espe.proyectou1.Service;

import edu.espe.proyectou1.Dto.UserWorkTeamDTO;
import edu.espe.proyectou1.Model.User;
import edu.espe.proyectou1.Model.UserWorkTeam;
import edu.espe.proyectou1.Model.WorkTeam;
import edu.espe.proyectou1.Repository.UserRepository;
import edu.espe.proyectou1.Repository.UserWorkTeamRepository;
import edu.espe.proyectou1.Repository.WorkTeamRepository;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserWorkTeamService {

    @Autowired
    private UserWorkTeamRepository userWorkTeamRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkTeamRepository workTeamRepository;

    @Autowired
    private ObservationRegistry observationRegistry;

    public List<UserWorkTeam> findAll() {
        return Observation.createNotStarted("userworkTeam-service.findAll", observationRegistry)
                .observe(() -> userWorkTeamRepository.findAll());
    }

    public ResponseEntity<?> save(UserWorkTeamDTO userWorkTeamDTO) {
        return Observation.createNotStarted("userworkTeam-service.save", observationRegistry)
                .observe(() -> {
                    // Validar que los campos no sean nulos
                    if (userWorkTeamDTO.getUser() == null || userWorkTeamDTO.getWorkTeam() == null) {
                        return new ResponseEntity<>("User and WorkTeam IDs cannot be null", HttpStatus.BAD_REQUEST);
                    }

                    UUID userId, workTeamId;

                    // Convertir los IDs a UUID y manejar posibles errores
                    try {
                        userId = UUID.fromString(userWorkTeamDTO.getUser());
                        workTeamId = UUID.fromString(userWorkTeamDTO.getWorkTeam());
                    } catch (IllegalArgumentException e) {
                        return new ResponseEntity<>("Invalid UUID format", HttpStatus.BAD_REQUEST);
                    }

                    // Buscar el User
                    Optional<User> userOptional = userRepository.findById(userId);
                    if (userOptional.isEmpty()) {
                        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
                    }

                    // Buscar el WorkTeam
                    Optional<WorkTeam> workTeamOptional = workTeamRepository.findById(workTeamId);
                    if (workTeamOptional.isEmpty()) {
                        return new ResponseEntity<>("WorkTeam not found", HttpStatus.NOT_FOUND);
                    }

                    // Crear y guardar el nuevo UserWorkTeam
                    User user = userOptional.get();
                    WorkTeam workTeam = workTeamOptional.get();
                    UserWorkTeam userWorkTeam = new UserWorkTeam();
                    userWorkTeam.setUser(user);
                    userWorkTeam.setWorkTeam(workTeam);

                    try {
                        UserWorkTeam savedUserWorkTeam = userWorkTeamRepository.save(userWorkTeam);
                        return new ResponseEntity<>(savedUserWorkTeam, HttpStatus.CREATED);
                    } catch (Exception e) {
                        return new ResponseEntity<>("Error saving UserWorkTeam: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                });
    }

    public ResponseEntity<?> deleteById(String id) {
        return Observation.createNotStarted("userworkTeam-service.deleteById", observationRegistry)
                .observe(() -> {
                    UUID uuid = UUID.fromString(id);
                Optional<UserWorkTeam> userWorkTeamOptional = userWorkTeamRepository.findById(uuid);
                if (userWorkTeamOptional.isPresent()) {
                    userWorkTeamRepository.deleteById(uuid);
                    return new ResponseEntity<>(userWorkTeamOptional.get(), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("UserWorkTeam not found", HttpStatus.NOT_FOUND);
                }});
    }

    public ResponseEntity<?> findById(String id) {
        return Observation.createNotStarted("userworkTeam-service.findById", observationRegistry)
                .observe(() -> {
                    UUID uuid = UUID.fromString(id);
                    Optional<UserWorkTeam> userWorkTeamOptional = userWorkTeamRepository.findById(uuid);
                    if (userWorkTeamOptional.isPresent()) {
                        UserWorkTeam userWorkTeam = userWorkTeamOptional.get();
                        UserWorkTeamDTO userWorkTeamDTO = new UserWorkTeamDTO();
                        userWorkTeamDTO.setUser(userWorkTeam.getUser().getId().toString()); // Asumiendo que User tiene un método getId()
                        userWorkTeamDTO.setWorkTeam(userWorkTeam.getWorkTeam().getId().toString()); // Similar para WorkTeam
                        return new ResponseEntity<>(userWorkTeamDTO, HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("UserWorkTeam not found", HttpStatus.NOT_FOUND);
                    }
                });
    }

    public ResponseEntity<?> updateById(String id, UserWorkTeamDTO userWorkTeamDTO) {
        return Observation.createNotStarted("userworkTeam-service.updateById", observationRegistry)
                .observe(() -> {
                    UUID uuid = UUID.fromString(id);
                    Optional<UserWorkTeam> userWorkTeamOptional = userWorkTeamRepository.findById(uuid);
                    if (userWorkTeamOptional.isPresent()) {
                        Optional<User> userOptional = userRepository.findById(UUID.fromString(userWorkTeamDTO.getUser()));
                        if (!userOptional.isPresent()) {
                            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
                        }

                        User user = userOptional.get();
                        Optional<WorkTeam> workTeamOptional = workTeamRepository.findById(UUID.fromString(userWorkTeamDTO.getWorkTeam()));
                        if (!workTeamOptional.isPresent()) {
                            return new ResponseEntity<>("WorkTeam not found", HttpStatus.NOT_FOUND);
                        }

                        WorkTeam workTeam = workTeamOptional.get();
                        UserWorkTeam existingUserWorkTeam = userWorkTeamOptional.get();
                        existingUserWorkTeam.setUser(user);
                        existingUserWorkTeam.setWorkTeam(workTeam);
                        userWorkTeamRepository.save(existingUserWorkTeam);
                        return new ResponseEntity<>("UserWorkTeam updated", HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("UserWorkTeam not found", HttpStatus.NOT_FOUND);
                    }
                });
    }
}