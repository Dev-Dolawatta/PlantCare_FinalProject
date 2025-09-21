package lk.ijse.plantcareapplication.repository;

import lk.ijse.plantcareapplication.entity.CareTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CareTaskRepository  extends JpaRepository<CareTask, Long> {
    // find all tasks for a specific plant
    List<CareTask> findByPlant_PlantId(Long plantId);

    // find all overdue tasks
    List<CareTask> findByIsOverdueTrue();

    // find all pending tasks (not completed yet)
    List<CareTask> findByCompletedDateIsNull();

    // optional: find all completed tasks
    List<CareTask> findByCompletedDateIsNotNull();
}
