package edu.espe.proyectou1.Repository;

import edu.espe.proyectou1.Model.UserWorkTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserWorkTeamRepository extends JpaRepository<UserWorkTeam, UUID> {
}