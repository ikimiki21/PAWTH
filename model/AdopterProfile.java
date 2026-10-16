package com.pawth.model;

public class AdopterProfile {
    private int userId;
    private String homeType;        // e.g., "House with yard"
    private boolean hasChildren;
    private boolean hasOtherPets;
    private String preferredAnimal; // e.g., "Dog · Medium"
    private String petExperience;

    public AdopterProfile() {}

    public AdopterProfile(int userId, String homeType, boolean hasChildren, boolean hasOtherPets, String preferredAnimal, String petExperience) {
        this.userId = userId;
        this.homeType = homeType;
        this.hasChildren = hasChildren;
        this.hasOtherPets = hasOtherPets;
        this.preferredAnimal = preferredAnimal;
        this.petExperience = petExperience;
    }

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getHomeType() { return homeType; }
    public void setHomeType(String homeType) { this.homeType = homeType; }

    public boolean isHasChildren() { return hasChildren; }
    public void setHasChildren(boolean hasChildren) { this.hasChildren = hasChildren; }

    public boolean isHasOtherPets() { return hasOtherPets; }
    public void setHasOtherPets(boolean hasOtherPets) { this.hasOtherPets = hasOtherPets; }

    public String getPreferredAnimal() { return preferredAnimal; }
    public void setPreferredAnimal(String preferredAnimal) { this.preferredAnimal = preferredAnimal; }

    public String getPetExperience() { return petExperience; }
    public void setPetExperience(String petExperience) { this.petExperience = petExperience; }
}