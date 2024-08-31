package edu.espe.proyectou1.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "user_work_teams")
public class UserWorkTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    //@JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //@JsonBackReference
    @ManyToOne
    @JoinColumn(name = "work_team_id", nullable = false)
    private WorkTeam workTeam;

    public UserWorkTeam() { }

    public UserWorkTeam(User user, WorkTeam workTeam) {
        this.user = user;
        this.workTeam = workTeam;
    }
}