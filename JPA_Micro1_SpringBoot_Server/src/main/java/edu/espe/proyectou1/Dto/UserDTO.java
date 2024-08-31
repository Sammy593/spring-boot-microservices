package edu.espe.proyectou1.Dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDTO {
    private UUID id;
    private String cedula;
    private String name;
    private String lastname;
    private String phone;
    private String email;
    private String state;
    private UUID idRol;
}
