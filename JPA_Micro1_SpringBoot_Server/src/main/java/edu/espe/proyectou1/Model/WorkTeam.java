package edu.espe.proyectou1.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "work_teams")
public class WorkTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private String description;

    //@JsonManagedReference
    @JsonIgnore
    @OneToMany(mappedBy = "workTeam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserWorkTeam> userWorkTeams;

    public WorkTeam() { }

    public WorkTeam(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
