package edu.espe.proyectou1.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private String responsible;
    private String state;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public Task(String name, String description, Date startDate, Date endDate, Project project, String responsible, String state) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.project = project;
        this.responsible = responsible;
        this.state = state;
    }

    public Task() {}

    public String getProjectId() {
        return this.project != null ? this.project.getId().toString() : null;
    }
}
