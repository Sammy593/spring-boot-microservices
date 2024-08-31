package edu.espe.proyectou1.Service;

import edu.espe.proyectou1.Dto.UserDTO;
import edu.espe.proyectou1.Model.Rol;
import edu.espe.proyectou1.Model.User;
import edu.espe.proyectou1.Payload.response.UserWithCounts;
import edu.espe.proyectou1.Repository.RolRepository;
import edu.espe.proyectou1.Repository.UserRepository;
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
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private ObservationRegistry observationRegistry;

    public List<UserDTO> findAll() {
        return Observation.createNotStarted("user-service.findAll", observationRegistry)
                .observe(() -> userRepository.findAll().stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList()));
    }

    public List<UserDTO> findAllCombo() {
        return Observation.createNotStarted("user-service.findAllCombo", observationRegistry)
                .observe(() -> userRepository.findAll().stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList()));
    }

    public ResponseEntity<?> save(UserDTO userDTO) {
        return Observation.createNotStarted("user-service.save", observationRegistry)
                .observe(() -> {
                    User user = new User();
                    user.setCedula(userDTO.getCedula());
                    user.setName(userDTO.getName());
                    user.setLastname(userDTO.getLastname());
                    user.setPhone(userDTO.getPhone());
                    user.setEmail(userDTO.getEmail());
                    user.setState(userDTO.getState());

                    if (userDTO.getIdRol() != null) {
                        Optional<Rol> rolOptional = rolRepository.findById(userDTO.getIdRol());
                        rolOptional.ifPresent(user::setRol);
                    }

                    User savedUser = userRepository.save(user);
                    return new ResponseEntity<>(convertToDTO(savedUser), HttpStatus.CREATED);
                });
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setCedula(user.getCedula());
        dto.setName(user.getName());
        dto.setLastname(user.getLastname());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setState(user.getState());
        if (user.getRol() != null) {
            dto.setIdRol(user.getRol().getId());
        }
        return dto;
    }

    public ResponseEntity<?> deleteById(String id) {
        return Observation.createNotStarted("user-service.deleteById", observationRegistry)
                .observe(() -> {
                    UUID uuid = UUID.fromString(id);
                    Optional<User> userOptional = userRepository.findById(uuid);
                    if (userOptional.isPresent()) {
                        userRepository.deleteById(uuid);
                        return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
                    }
                });
    }

    public ResponseEntity<?> findById(String id) {
        return Observation.createNotStarted("user-service.findById", observationRegistry)
                .observe(() -> {
                    UUID uuid = UUID.fromString(id);
                    Optional<User> userOptional = userRepository.findById(uuid);
                    if (userOptional.isPresent()) {
                        User user = userOptional.get();
                        UserDTO userDTO = convertToDTO(user);

                        RestTemplate restTemplate = new RestTemplate();
                        // Cuantos proyectos lidera
                        String url1 = "http://localhost:8085/mqp/m2/project/countProjectsByUserId/" + id;
                        ResponseEntity<String> response1 = restTemplate.getForEntity(url1, String.class);
                        String countProject = response1.getBody();
                        // Cuantas tareas realiza
                        String url2 = "http://localhost:8085/mqp/m2/task/countTasksByUserId/" + id;
                        ResponseEntity<String> response2 = restTemplate.getForEntity(url2, String.class);
                        String countTask = response2.getBody();

                        UserWithCounts userRes = new UserWithCounts();
                        userRes.setUser(userDTO); // Esta línea debería funcionar ahora si UserWithCounts acepta UserDTO
                        userRes.setNumProjects(countProject);
                        userRes.setNumTasks(countTask);

                        if (user.getRol() != null) {
                            Optional<Rol> rolOptional = rolRepository.findById(user.getRol().getId());
                            rolOptional.ifPresent(rol -> userRes.setRolName(rol.getName()));
                        } else {
                            userRes.setRolName("No role assigned");
                        }

                        return new ResponseEntity<>(userRes, HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
                    }
                });
    }

    public ResponseEntity<?> updateById(String id, UserDTO userDTO) {
        return Observation.createNotStarted("user-service.updateById", observationRegistry)
                .observe(() -> {
                    UUID uuid = UUID.fromString(id);
                    Optional<User> userOptional = userRepository.findById(uuid);
                    if (userOptional.isPresent()) {
                        User existingUser = userOptional.get();
                        existingUser.setCedula(userDTO.getCedula());
                        existingUser.setName(userDTO.getName());
                        existingUser.setLastname(userDTO.getLastname());
                        existingUser.setPhone(userDTO.getPhone());
                        existingUser.setEmail(userDTO.getEmail());
                        existingUser.setState(userDTO.getState());

                        if (userDTO.getIdRol() != null) {
                            Optional<Rol> rolOptional = rolRepository.findById(userDTO.getIdRol());
                            rolOptional.ifPresent(existingUser::setRol);
                        } else {
                            existingUser.setRol(null);
                        }

                        User updatedUser = userRepository.save(existingUser);
                        return new ResponseEntity<>(convertToDTO(updatedUser), HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
                    }
                });
    }
}
