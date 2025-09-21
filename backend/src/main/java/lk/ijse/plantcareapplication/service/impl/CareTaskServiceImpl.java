package lk.ijse.plantcareapplication.service.impl;

import lk.ijse.plantcareapplication.entity.CareTask;
import lk.ijse.plantcareapplication.entity.Plant;
import lk.ijse.plantcareapplication.repository.CareTaskRepository;
import lk.ijse.plantcareapplication.repository.PlantRepository;
import lk.ijse.plantcareapplication.service.CareTaskService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CareTaskServiceImpl implements CareTaskService {

    @Autowired
    private CareTaskRepository careTaskRepository;

    @Autowired
    private PlantRepository plantRepository;

    private LocalDate completedDate;


    @Override
    public List<CareTask> getAllTasks() {
        return careTaskRepository.findAll();
    }

    @Override
    public CareTask saveTask(CareTask task) {

        if (task.getPlant() != null && task.getPlant().getPlantId() != null) {
            Plant plant = plantRepository.findById(task.getPlant().getPlantId())
                    .orElseThrow(() -> new RuntimeException("Plant not found"));
            task.setPlant(plant);
        }

        if (task.getScheduledDate() == null) {
            task.setScheduledDate(LocalDate.now());
        }

        task.setOverdue(false);
        task.setCompletedDate(null);

        return careTaskRepository.save(task);
    }


    @Override
    public CareTask updateTask(Long id, CareTask updatedTask) {
        return careTaskRepository.findById(id).map(task -> {
            task.setTaskType(updatedTask.getTaskType());
            task.setScheduledDate(updatedTask.getScheduledDate());
            task.setCompletedDate(updatedTask.getCompletedDate());
            task.setNotes(updatedTask.getNotes());
            task.setOverdue(updatedTask.isOverdue());

            if (updatedTask.getPlant() != null && updatedTask.getPlant().getPlantId() != null) {
                Plant plant = plantRepository.findById(updatedTask.getPlant().getPlantId())
                        .orElseThrow(() -> new RuntimeException("Plant not found"));
                task.setPlant(plant);
            }

            return careTaskRepository.save(task);
        }).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @Override
    public CareTask completeTask(Long id) {
        return careTaskRepository.findById(id).map(task -> {
            task.setCompletedDate(LocalDate.now());
            task.setOverdue(false);
            return careTaskRepository.save(task);
        }).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @Override
    public void deleteTask(Long id) {
        careTaskRepository.deleteById(id);
    }

    @Override
    public List<CareTask> getTasksByPlantId(Long plantId) {
        return careTaskRepository.findByPlant_PlantId(plantId);
    }

    @Override
    public List<CareTask> getOverdueTasks() {
        return careTaskRepository.findByIsOverdueTrue();
    }

    @Override
    public List<CareTask> getPendingTasks() {
        return careTaskRepository.findByCompletedDateIsNull();
    }

    @Override
    public List<CareTask> getTasksByUser(Long userId) {
        return careTaskRepository.findAll().stream()
                .filter(task -> task.getPlant() != null
                        && task.getPlant().getUser() != null
                        && task.getPlant().getUser().getUserId().equals(userId))
                .toList();
    }

    @Override
    public void markTaskAsCompleted(Long taskId) {
        CareTask task = careTaskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setCompletedDate(LocalDate.now());
        careTaskRepository.save(task);
    }


}
