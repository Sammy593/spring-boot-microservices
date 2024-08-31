package edu.espe.proyectou1.Dto;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class TaskDTO {
    private UUID id;
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private String responsible;
    private String state;
    private UUID projectId;
}
