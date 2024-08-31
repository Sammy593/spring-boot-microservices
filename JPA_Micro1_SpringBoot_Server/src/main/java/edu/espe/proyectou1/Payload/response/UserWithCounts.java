package edu.espe.proyectou1.Payload.response;

import edu.espe.proyectou1.Dto.UserDTO;
import edu.espe.proyectou1.Model.User;
import lombok.Data;

@Data
public class UserWithCounts {
    private UserDTO user;
    private String rolName;
    private String numProjects;
    private String numTasks;
}
