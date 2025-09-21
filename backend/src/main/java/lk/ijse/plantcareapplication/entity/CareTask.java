package lk.ijse.plantcareapplication.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "careTask")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CareTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;
    private  String taskType;
    private LocalDate scheduledDate;
    private LocalDate completedDate;
    private boolean isOverdue;
    private String notes;

    @ManyToOne()
    @JoinColumn(name = "plantId")
    @JsonIgnoreProperties({"careTasks","user"})
   private Plant plant;


}
