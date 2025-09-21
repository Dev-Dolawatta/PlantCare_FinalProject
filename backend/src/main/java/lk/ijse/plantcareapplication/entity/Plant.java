package lk.ijse.plantcareapplication.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "plant")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long plantId;
    private String name;
    @JsonIgnore
    private String image;
    private String type;
    private String environmentType;
    private Integer age;
    @Enumerated(EnumType.STRING)
    private PlantStatus healthStatus;
    @Enumerated(EnumType.STRING)
    private PlantPriority priorityLvl;
    private Boolean isArchived = false;
    private Boolean isFavourite;

    @OneToOne(mappedBy = "plant")
    private CareCondition careCondition;

    @OneToMany(mappedBy = "plant",cascade = CascadeType.ALL)
    @JsonIgnoreProperties("plant")
    private List<CareTask> careTasks;

    @ManyToOne
    @JoinColumn(name = "userId")
    private Users user;

//    @OneToOne(mappedBy = "plant")
//    private Users userFav;

    @OneToMany(mappedBy = "plant",cascade = CascadeType.ALL)
    private List<Note> notes;

    @OneToMany(mappedBy = "plant",cascade = CascadeType.ALL)
    private List<CareHistory> careHistories;
}
