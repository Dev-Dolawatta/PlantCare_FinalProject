package lk.ijse.plantcareapplication.repository;

import lk.ijse.plantcareapplication.entity.Plant;
import lk.ijse.plantcareapplication.entity.PlantStatus;
import lk.ijse.plantcareapplication.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {

//    List<Plant> findByNameContainingIgnoreCase(String name);
    List<Plant> findByNameContainingIgnoreCaseAndUserOrderByNameAsc(String name, Users user);
    List<Plant> findByTypeAndUser(String type, Users user);
    List<Plant> findByHealthStatus(PlantStatus status);
    List<Plant> findByIsFavouriteTrue();
    List<Plant> findByUser(Users user);
    List<Plant> findByUser_UserId(Long userId);
    // Get all ACTIVE (non-archived) plants for a user
    List<Plant> findByUser_UserIdAndIsArchivedFalse(Long userId);

    // Get all ARCHIVED plants for a user
    List<Plant> findByUser_UserIdAndIsArchivedTrue(Long userId);


}
