package com.pawth.model;

import java.sql.Timestamp;

public class RescueCase {
    private int caseId;               // e.g., #102, #108, #110
    private int reporterId;
    private String animalType;        // "Dog", "Cat"
    private String approximateSize;   // "Small", "Medium", "Large"
    private String locationLandmark;  // e.g., "Maharlika Hwy"
    private String barangay;          // e.g., "Barangay Maligaya"
    private String areaStreet;        // e.g., "beside public market"
    private String photoUrl;
    private String conditionFlags;    // e.g., "Injured, Near busy road"
    private String additionalNotes;
    private PriorityLevel priorityLevel; // CRITICAL, HIGH, MEDIUM, LOW
    private int priorityScore;         // e.g., 12, 9, 6
    private CaseStatus caseStatus;     // REPORTED, VERIFIED, RESCUE_ASSIGNED, RESCUED
    private Integer assignedHandlerId;
    private String handlerNotes;
    private Timestamp createdAt;

    // Joined object helper for UI display
    private String reporterName;
    private String assignedHandlerName;

    public enum PriorityLevel {
        CRITICAL, HIGH, MEDIUM, LOW
    }

    public enum CaseStatus {
        REPORTED, VERIFIED, RESCUE_ASSIGNED, RESCUED
    }

    public RescueCase() {}

    // Getters and Setters
    public int getCaseId() { return caseId; }
    public void setCaseId(int caseId) { this.caseId = caseId; }

    public int getReporterId() { return reporterId; }
    public void setReporterId(int reporterId) { this.reporterId = reporterId; }

    public String getAnimalType() { return animalType; }
    public void setAnimalType(String animalType) { this.animalType = animalType; }

    public String getApproximateSize() { return approximateSize; }
    public void setApproximateSize(String approximateSize) { this.approximateSize = approximateSize; }

    public String getLocationLandmark() { return locationLandmark; }
    public void setLocationLandmark(String locationLandmark) { this.locationLandmark = locationLandmark; }

    public String getBarangay() { return barangay; }
    public void setBarangay(String barangay) { this.barangay = barangay; }

    public String getAreaStreet() { return areaStreet; }
    public void setAreaStreet(String areaStreet) { this.areaStreet = areaStreet; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public String getConditionFlags() { return conditionFlags; }
    public void setConditionFlags(String conditionFlags) { this.conditionFlags = conditionFlags; }

    public String getAdditionalNotes() { return additionalNotes; }
    public void setAdditionalNotes(String additionalNotes) { this.additionalNotes = additionalNotes; }

    public PriorityLevel getPriorityLevel() { return priorityLevel; }
    public void setPriorityLevel(PriorityLevel priorityLevel) { this.priorityLevel = priorityLevel; }

    public int getPriorityScore() { return priorityScore; }
    public void setPriorityScore(int priorityScore) { this.priorityScore = priorityScore; }

    public CaseStatus getCaseStatus() { return caseStatus; }
    public void setCaseStatus(CaseStatus caseStatus) { this.caseStatus = caseStatus; }

    public Integer getAssignedHandlerId() { return assignedHandlerId; }
    public void setAssignedHandlerId(Integer assignedHandlerId) { this.assignedHandlerId = assignedHandlerId; }

    public String getHandlerNotes() { return handlerNotes; }
    public void setHandlerNotes(String handlerNotes) { this.handlerNotes = handlerNotes; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getReporterName() { return reporterName; }
    public void setReporterName(String reporterName) { this.reporterName = reporterName; }

    public String getAssignedHandlerName() { return assignedHandlerName; }
    public void setAssignedHandlerName(String assignedHandlerName) { this.assignedHandlerName = assignedHandlerName; }
}