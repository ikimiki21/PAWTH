package com.pawth.model;

public class Animal {
    private int animalId;
    private String name;              // e.g., "Buddy", "Luna"
    private String species;           // e.g., "Dog", "Cat"
    private String breed;             // e.g., "Aspen mix", "Domestic shorthair"
    private int ageYears;
    private String gender;            // "Male", "Female"
    private String healthStatus;      // e.g., "Healthy · Fully Vaccinated"
    private String visitSchedule;     // e.g., "Sat, 10:00 AM"
    private AdoptionStatus adoptionStatus; 
    private String photoUrl;          // Asset filename/path
    private String personalityTraits; // e.g., "Playful, People-friendly"
    private int matchPercentage;      // Dynamically calculated per user

    public enum AdoptionStatus {
        AVAILABLE, ON_HOLD, MEDICAL_CARE, ADOPTED
    }

    public Animal() {}

    public Animal(int animalId, String name, String species, String breed, int ageYears, String gender, String healthStatus, String visitSchedule, AdoptionStatus adoptionStatus, String photoUrl, String personalityTraits) {
        this.animalId = animalId;
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.ageYears = ageYears;
        this.gender = gender;
        this.healthStatus = healthStatus;
        this.visitSchedule = visitSchedule;
        this.adoptionStatus = adoptionStatus;
        this.photoUrl = photoUrl;
        this.personalityTraits = personalityTraits;
    }

    // Getters and Setters
    public int getAnimalId() { return animalId; }
    public void setAnimalId(int animalId) { this.animalId = animalId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }

    public int getAgeYears() { return ageYears; }
    public void setAgeYears(int ageYears) { this.ageYears = ageYears; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getHealthStatus() { return healthStatus; }
    public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }

    public String getVisitSchedule() { return visitSchedule; }
    public void setVisitSchedule(String visitSchedule) { this.visitSchedule = visitSchedule; }

    public AdoptionStatus getAdoptionStatus() { return adoptionStatus; }
    public void setAdoptionStatus(AdoptionStatus adoptionStatus) { this.adoptionStatus = adoptionStatus; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public String getPersonalityTraits() { return personalityTraits; }
    public void setPersonalityTraits(String personalityTraits) { this.personalityTraits = personalityTraits; }

    public int getMatchPercentage() { return matchPercentage; }
    public void setMatchPercentage(int matchPercentage) { this.matchPercentage = matchPercentage; }
}