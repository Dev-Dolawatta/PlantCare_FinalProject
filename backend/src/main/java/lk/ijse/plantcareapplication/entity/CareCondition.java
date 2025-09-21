package lk.ijse.plantcareapplication.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "careCondition")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CareCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long careId;
    private String waterFrequency;
    private String fertiFrequency;
    private String remainderTime;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "plantId")
    private Plant plant;


}
