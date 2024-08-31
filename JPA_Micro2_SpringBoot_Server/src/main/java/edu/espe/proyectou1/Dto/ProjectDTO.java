package edu.espe.proyectou1.Dto;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class ProjectDTO {
    private UUID id;
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private Double progress;
    private String state;
    private String idLeader;
    private List<UUID> taskIds;
    private UUID companyId;
}
