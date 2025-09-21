package lk.ijse.plantcareapplication.service.impl;

import lk.ijse.plantcareapplication.entity.Plant;
import lk.ijse.plantcareapplication.entity.PlantPriority;
import lk.ijse.plantcareapplication.entity.PlantStatus;
import lk.ijse.plantcareapplication.entity.Users;
import lk.ijse.plantcareapplication.repository.PlantRepository;
import lk.ijse.plantcareapplication.repository.UsersRepository;
import lk.ijse.plantcareapplication.service.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
public class PlantServiceImpl implements PlantService {
    private final PlantRepository plantRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public PlantServiceImpl(PlantRepository plantRepository, UsersRepository usersRepository) {
        this.plantRepository = plantRepository;
        this.usersRepository = usersRepository;
    }
    @Override
    public Plant savePlant(Long userId, Plant plant) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        plant.setUser(user);
        return plantRepository.save(plant);
    }

    @Override
    public Plant updatePlant(Plant plant) {
        if (plant.getPlantId() == null || !plantRepository.existsById(plant.getPlantId())) {
            throw new IllegalArgumentException("Plant does not exist with ID: " + plant.getPlantId());
        }
        return plantRepository.save(plant);
    }

    @Override
    public void deletePlant(Long plantId) {
        if (!plantRepository.existsById(plantId)) {
            throw new IllegalArgumentException("Plant does not exist with ID: " + plantId);
        }
        plantRepository.deleteById(plantId);
    }

    @Override
    public Optional<Plant> getPlantById(Long plantId) {
        return plantRepository.findById(plantId);
    }

    @Override
    public List<Plant> getAllPlants(Long userId) {
        List<Plant> plants = plantRepository.findByUser_UserId(userId);
        if (plants.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + userId);
        }
        return plants;
    }


    //    @Override
//    public List<Plant> searchPlantsByName(String name) {
//        return plantRepository.findByNameContainingIgnoreCase(name);
//    }
    public List<Plant> searchPlantsByName(Long userId, String name) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return plantRepository.findByNameContainingIgnoreCaseAndUserOrderByNameAsc(name, user);

    }

    @Override
    public List<Plant> getPlantsByType(Long userId, String type) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return plantRepository.findByTypeAndUser(type, user);
    }

    @Override
    public List<Plant> getPlantsByHealthStatus(PlantStatus status) {
        return plantRepository.findByHealthStatus(status);
    }

    @Override
    public List<Plant> getFavouritePlants() {
        return plantRepository.findByIsFavouriteTrue();
    }

    @Override
    public Plant archivePlant(Long plantId) {
        return plantRepository.findById(plantId)
                .map(plant -> {
                    plant.setIsArchived(true);
                    return plantRepository.save(plant);
                })
                .orElseThrow(() -> new RuntimeException("Plant not found with ID: " + plantId));
    }

    @Override
    public List<Plant> getActivePlants(Long userId) {
        return plantRepository.findByUser_UserIdAndIsArchivedFalse(userId);
    }

    @Override
    public List<Plant> getArchivedPlants(Long userId) {
        return plantRepository.findByUser_UserIdAndIsArchivedTrue(userId);
    }

    @Override
    public Plant addPlant(Long userId, String name, MultipartFile imageFile, String environmentType, Integer age, String type, PlantStatus healthStatus, PlantPriority priorityLvl, Boolean isArchived, Boolean isFavourite) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Plant plant = new Plant();
        plant.setName(name);
        plant.setEnvironmentType(environmentType);
        plant.setAge(age);
        plant.setType(type);
        plant.setHealthStatus(healthStatus);
        plant.setPriorityLvl(priorityLvl);
        plant.setIsArchived(isArchived);
        plant.setIsFavourite(isFavourite);
        plant.setUser(user);

        // handle image
        if (imageFile != null && !imageFile.isEmpty()) {
            String originalFileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
            // Generate unique filename with timestamp
            String fileName = System.currentTimeMillis() + "_" + originalFileName;
            Path uploadPath = Paths.get("uploads");
            try {
                Files.createDirectories(uploadPath);
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                plant.setImage(fileName);
            } catch (IOException e) {
                throw new RuntimeException("Could not save image file: " + fileName, e);
            }
        }

        return plantRepository.save(plant);
    }

    @Override
    public int getTotalPlants(Long userId) {
        return plantRepository.findByUser_UserId(userId).size();
    }

    @Override
    public int getFavouritePlants(Long userId) {
        return (int) plantRepository.findByUser_UserId(userId)
                .stream()
                .filter(Plant::getIsFavourite)
                .count();
    }

    @Override
    public Plant restorePlant(Long plantId) {
        return plantRepository.findById(plantId)
                .map(plant -> {
                    plant.setIsArchived(false);
                    return plantRepository.save(plant);
                })
                .orElseThrow(() -> new RuntimeException("Plant not found with ID: " + plantId));
    }


}
