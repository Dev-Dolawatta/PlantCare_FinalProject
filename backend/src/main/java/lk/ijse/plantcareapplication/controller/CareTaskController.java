package lk.ijse.plantcareapplication.controller;

import lk.ijse.plantcareapplication.entity.CareTask;
import lk.ijse.plantcareapplication.entity.Plant;
import lk.ijse.plantcareapplication.repository.CareTaskRepository;
import lk.ijse.plantcareapplication.repository.PlantRepository;
import lk.ijse.plantcareapplication.service.CareTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin
public class CareTaskController {
    private final CareTaskService careTaskService;

    public CareTaskController(CareTaskService careTaskService) {
        this.careTaskService = careTaskService;
    }

    @GetMapping
    public List<CareTask> getAllTasks() {
        return careTaskService.getAllTasks();
    }

    @PostMapping
    public CareTask saveTask(@RequestBody CareTask task){
        return careTaskService.saveTask(task);

    }
    @PutMapping("/{id}")
    public CareTask updateTask(@PathVariable Long id, @RequestBody CareTask updatedTask) {
            return careTaskService.updateTask(id,updatedTask);

    }
//    @PutMapping("/{id}/complete")
//    public CareTask completeTask(@PathVariable Long id) {
//        return careTaskService.completeTask(id);
//    }
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
         careTaskService.deleteTask(id);
    }
    @GetMapping("/user/{userId}")
    public List<CareTask> getTasksByUser(@PathVariable Long userId) {
        return careTaskService.getTasksByUser(userId);
    }

    @PutMapping("/api/tasks/{taskId}/complete")
    public ResponseEntity<String> completeTask(@PathVariable Long taskId) {
        try {
            careTaskService.markTaskAsCompleted(taskId);
            return ResponseEntity.ok("Task marked as completed");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to complete task");
        }
    }
}
