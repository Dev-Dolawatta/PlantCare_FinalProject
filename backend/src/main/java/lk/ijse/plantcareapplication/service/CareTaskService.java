package lk.ijse.plantcareapplication.service;

import lk.ijse.plantcareapplication.entity.CareTask;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CareTaskService {
    List<CareTask> getAllTasks();
    CareTask saveTask(CareTask task);
    CareTask updateTask(Long id, CareTask task);
    CareTask completeTask(Long id);
    void deleteTask(Long id);
    List<CareTask> getTasksByPlantId(Long plantId);
    List<CareTask> getOverdueTasks();
    List<CareTask> getPendingTasks();
    List<CareTask> getTasksByUser(Long userId);

    void markTaskAsCompleted(Long taskId);
}
