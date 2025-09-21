package lk.ijse.plantcareapplication.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "careHistory")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CareHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;
    private String taskType;
    private LocalDate completedDate;
    private String statusBefore;
    private String statusNow;

    @ManyToOne()
    @JoinColumn(name = "plantId")
    private Plant plant;

}
