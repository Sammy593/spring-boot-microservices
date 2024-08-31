package edu.espe.proyectou1.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String cedula;
    private String name;
    private String lastname;
    private String phone;
    private String email;
    private String state;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "rol_id", nullable = true)
    private Rol rol;

    @JsonIgnore
    //@JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserWorkTeam> userWorkTeams;

    public User() {
    }

    public User(String cedula, String name, String lastname, String phone, String email, String state, Rol rol) {
        this.cedula = cedula;
        this.name = name;
        this.lastname = lastname;
        this.phone = phone;
        this.email = email;
        this.state = state;
        this.rol = rol;
    }
}
