package edu.espe.proyectou1.Repository;

import edu.espe.proyectou1.Model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public interface RolRepository extends JpaRepository<Rol, UUID> {
}