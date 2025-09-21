package lk.ijse.plantcareapplication.dto;

public class PlantCountDTO {
    private int totalPlants;
    private int favouritePlants;

    public PlantCountDTO(int totalPlants, int favouritePlants) {
        this.totalPlants = totalPlants;
        this.favouritePlants = favouritePlants;
    }

    public int getTotalPlants() {
        return totalPlants;
    }

    public void setTotalPlants(int totalPlants) {
        this.totalPlants = totalPlants;
    }

    public int getFavouritePlants() {
        return favouritePlants;
    }

    public void setFavouritePlants(int favouritePlants) {
        this.favouritePlants = favouritePlants;
    }
}
