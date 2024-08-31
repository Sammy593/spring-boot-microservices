package edu.espe.proyectou1.Payload.response;

import edu.espe.proyectou1.Dto.TaskDTO;
import edu.espe.proyectou1.Model.Task;
import lombok.Data;

@Data
public class TaskWithResponsible {
    private TaskDTO task;
    private Object responsibleObject;
}
