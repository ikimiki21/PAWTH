package com.pawth.model;

import java.sql.Timestamp;

public class AdoptionApplication {
    private int applicationId;
    private int applicantId;
    private int animalId;
    private int matchPercentage; // e.g., 94%, 89%
    private ApplicationStatus status; 
    private Timestamp createdAt;

    // Joined fields for UI rendering
    private String applicantName;
    private String animalName;
    private String animalSpecies;

    public enum ApplicationStatus {
        NEW, CONSULTATION, HOME_CHECK, APPROVED, REJECTED
    }

    public AdoptionApplication() {}

    // Getters and Setters
    public int getApplicationId() { return applicationId; }
    public void setApplicationId(int applicationId) { this.applicationId = applicationId; }

    public int getApplicantId() { return applicantId; }
    public void setApplicantId(int applicantId) { this.applicantId = applicantId; }

    public int getAnimalId() { return animalId; }
    public void setAnimalId(int animalId) { this.animalId = animalId; }

    public int getMatchPercentage() { return matchPercentage; }
    public void setMatchPercentage(int matchPercentage) { this.matchPercentage = matchPercentage; }

    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getApplicantName() { return applicantName; }
    public void setApplicantName(String applicantName) { this.applicantName = applicantName; }

    public String getAnimalName() { return animalName; }
    public void setAnimalName(String animalName) { this.animalName = animalName; }

    public String getAnimalSpecies() { return animalSpecies; }
    public void setAnimalSpecies(String animalSpecies) { this.animalSpecies = animalSpecies; }
}