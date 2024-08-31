package edu.espe.proyectou1.Repository;

import edu.espe.proyectou1.Model.Project;
import edu.espe.proyectou1.Model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByProject_Id(UUID projectId);
    List<Task> findByResponsible(String responsible);
}