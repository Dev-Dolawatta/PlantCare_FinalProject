package lk.ijse.plantcareapplication.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//@Builder
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String username;
    private String email;
    private String password;
    private Integer plantLimit = 10;
    private LocalDate lastLogin;
    private Boolean isActive = true;


    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Plant> plants;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "favouritePlantId")
//    private Plant plant;

    public Long getId() {
        return userId;
    }
    public void setId(Long id) {
       this.userId = id;
    }


}
