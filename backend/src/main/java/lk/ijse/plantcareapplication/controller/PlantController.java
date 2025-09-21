package lk.ijse.plantcareapplication.controller;


import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpSession;
import lk.ijse.plantcareapplication.dto.PlantCountDTO;
import lk.ijse.plantcareapplication.entity.Plant;
import lk.ijse.plantcareapplication.entity.PlantPriority;
import lk.ijse.plantcareapplication.entity.PlantStatus;
import lk.ijse.plantcareapplication.entity.Users;
import lk.ijse.plantcareapplication.repository.PlantRepository;
import lk.ijse.plantcareapplication.repository.UsersRepository;
import lk.ijse.plantcareapplication.service.PlantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
@MultipartConfig
@CrossOrigin
@RestController
@RequestMapping("plant")

public class PlantController {


    private final PlantService plantService;


    @Autowired
    public PlantController( PlantService plantService) {

        this.plantService = plantService;
    }

    @PutMapping("/update")
    public ResponseEntity<Plant> updatePlant(@RequestBody Plant plant) {
        Plant updatedPlant = plantService.updatePlant(plant);
        return new ResponseEntity<>(updatedPlant, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{plantId}")
    public ResponseEntity<String> deletePlant(@PathVariable Long plantId) {
        plantService.deletePlant(plantId);
        return new ResponseEntity<>("Plant deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/{plantId}")
    public ResponseEntity<Plant> getPlantById(@PathVariable Long plantId) {
        Optional<Plant> plant = plantService.getPlantById(plantId);
        return plant.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
//    @GetMapping("/all/{userId}")
//    public ResponseEntity<List<Plant>> getPlantsByUser(@PathVariable Long userId) {
//        Users user = usersRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        List<Plant> plants = plantRepository.findByUser(user);
//        return ResponseEntity.ok(plants);
//    }


    //    @GetMapping("/search")
//    public ResponseEntity<List<Plant>> searchPlantsByName(@RequestParam String name) {
//        List<Plant> plants = plantService.searchPlantsByName(name);
//        return new ResponseEntity<>(plants, HttpStatus.OK);
//    }
    @GetMapping("/search/name/{userId}")
    public List<Plant> searchByName(@PathVariable Long userId, @RequestParam String name) {
        return plantService.searchPlantsByName(userId, name);
    }

    //    @GetMapping("/type/{type}")
//    public ResponseEntity<List<Plant>> getPlantsByType(@PathVariable String type) {
//        List<Plant> plants = plantService.getPlantsByType(type);
//        return new ResponseEntity<>(plants, HttpStatus.OK);
//    }
    @GetMapping("/plants/type/{userId}")
    public List<Plant> getPlantsByType(@PathVariable Long userId, @RequestParam String type) {
        return plantService.getPlantsByType(userId, type);
    }


    @GetMapping("/health/{status}")
    public ResponseEntity<List<Plant>> getPlantsByHealthStatus(@PathVariable PlantStatus status) {
        List<Plant> plants = plantService.getPlantsByHealthStatus(status);
        return new ResponseEntity<>(plants, HttpStatus.OK);
    }

    @GetMapping("/favourites")
    public ResponseEntity<List<Plant>> getFavouritePlants() {
        List<Plant> plants = plantService.getFavouritePlants();
        return new ResponseEntity<>(plants, HttpStatus.OK);
    }
    //    @PostMapping("/add/{userId}")
//    public ResponseEntity<Plant> addPlant(
//            @PathVariable Long userId,
//            @RequestParam("name") String name,
//            @RequestParam("image") MultipartFile imageFile,
//            @RequestParam("environmentType") String environmentType,
//            @RequestParam("age") Integer age,
//            @RequestParam("type") String type,
//            @RequestParam("healthStatus") PlantStatus healthStatus,
//            @RequestParam("priorityLvl") PlantPriority priorityLvl,
//            @RequestParam("isArchived") Boolean isArchived,
//            @RequestParam("isFavourite") Boolean isFavourite
//    ) {
//        // Fetch user
//        Users user = usersRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // Create plant
//        Plant plant = new Plant();
//        plant.setName(name);
//        plant.setEnvironmentType(environmentType);
//        plant.setAge(age);
//        plant.setType(type);
//        plant.setHealthStatus(healthStatus);
//        plant.setPriorityLvl(priorityLvl);
//        plant.setIsArchived(isArchived);
//        plant.setIsFavourite(isFavourite);
//        plant.setUser(user);
//
//        // Handle image upload
//        if (!imageFile.isEmpty()) {
//            String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
//            Path uploadPath = Paths.get("uploads");
//            try {
//                Files.createDirectories(uploadPath);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            Path filePath = uploadPath.resolve(fileName);
//            try {
//                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//
//            plant.setImage(fileName);
//
//
//        }
//
//        // Save plant
//        Plant savedPlant = plantRepository.save(plant);
//        return new ResponseEntity<>(savedPlant, HttpStatus.CREATED);
//    }
    @PostMapping("/add/{userId}")
    public ResponseEntity<Plant> addPlant(
            @PathVariable Long userId,
            @RequestParam("name") String name,
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam("environmentType") String environmentType,
            @RequestParam("age") Integer age,
            @RequestParam("type") String type,
            @RequestParam("healthStatus") PlantStatus healthStatus,
            @RequestParam("priorityLvl") PlantPriority priorityLvl,
            @RequestParam("isArchived") Boolean isArchived,
            @RequestParam("isFavourite") Boolean isFavourite
    ) {
        Plant savedPlant = plantService.addPlant(
                userId, name, imageFile, environmentType, age, type,
                healthStatus, priorityLvl, isArchived, isFavourite
        );
        return new ResponseEntity<>(savedPlant, HttpStatus.CREATED);
    }

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        System.out.println("=== IMAGE REQUEST DEBUG ===");
        System.out.println("Requested filename: " + filename);

        try {
            Path filePath = Paths.get("uploads/").resolve(filename).normalize();
            System.out.println("Looking for file at: " + filePath.toAbsolutePath());

            Resource resource = new UrlResource(filePath.toUri());
            System.out.println("Resource exists: " + resource.exists());

            if (!resource.exists()) {
                System.out.println("❌ File not found: " + filename);
                return ResponseEntity.notFound().build();
            }

            // Determine content type based on file extension
            String contentType = "application/octet-stream";
            if (filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".jpeg")) {
                contentType = "image/jpeg";
            } else if (filename.toLowerCase().endsWith(".png")) {
                contentType = "image/png";
            } else if (filename.toLowerCase().endsWith(".gif")) {
                contentType = "image/gif";
            }

            System.out.println("✅ Serving image: " + filename + " with content type: " + contentType);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);

        } catch (MalformedURLException e) {
            System.out.println("❌ Malformed URL exception: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/test-image")
    public ResponseEntity<String> testImageEndpoint() {
        return ResponseEntity.ok("Image endpoint is working! Available images in uploads folder:");
    }


    @GetMapping("/all/{userId}")
    public List<Plant> getAllPlantsForUser(@PathVariable Long userId) {
        return plantService.getAllPlants(userId);
    }

    @PutMapping("/{plantId}/archive")
    public ResponseEntity<Plant> archivePlant(@PathVariable Long plantId) {
        try {
            Plant archivedPlant = plantService.archivePlant(plantId);
            return ResponseEntity.ok(archivedPlant);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    // Active plants
    @GetMapping("/active/{userId}")
    public List<Plant> getActivePlants(@PathVariable Long userId) {
        return plantService.getActivePlants(userId);
    }

    // Archived plants
    @GetMapping("/archived/{userId}")
    public List<Plant> getArchivedPlants(@PathVariable Long userId) {
        return plantService.getArchivedPlants(userId);
    }
    @GetMapping("/counts/{userId}")
    public ResponseEntity<PlantCountDTO> getPlantCounts(@PathVariable Long userId) {
        int total = plantService.getTotalPlants(userId);
        int fav = plantService.getFavouritePlants(userId);

        PlantCountDTO dto = new PlantCountDTO(total, fav);
        return ResponseEntity.ok(dto);
    }
    @PutMapping("/{plantId}/restore")
    public ResponseEntity<Plant> restorePlant(@PathVariable Long plantId) {
        try {
            Plant restoredPlant = plantService.restorePlant(plantId);
            return ResponseEntity.ok(restoredPlant);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
