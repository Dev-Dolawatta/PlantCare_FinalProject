package lk.ijse.plantcareapplication.service;

import lk.ijse.plantcareapplication.entity.Plant;
import lk.ijse.plantcareapplication.entity.PlantPriority;
import lk.ijse.plantcareapplication.entity.PlantStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public interface PlantService {
    Plant savePlant(Long userId, Plant plant);

    Plant updatePlant(Plant plant);

    void deletePlant(Long plantId);

    Optional<Plant> getPlantById(Long plantId);

    List<Plant> getAllPlants(Long userId);

    List<Plant> searchPlantsByName(Long userId, String name);

    List<Plant> getPlantsByType(Long userId, String type);

    List<Plant> getPlantsByHealthStatus(PlantStatus status);

    List<Plant> getFavouritePlants();

    Plant archivePlant(Long plantId);

    List<Plant> getActivePlants(Long userId);

    List<Plant> getArchivedPlants(Long userId);

    Plant addPlant(Long userId, String name, MultipartFile imageFile, String environmentType, Integer age, String type, PlantStatus healthStatus, PlantPriority priorityLvl, Boolean isArchived, Boolean isFavourite);

    int getTotalPlants(Long userId);

    int getFavouritePlants(Long userId);

    Plant restorePlant(Long plantId);
}
