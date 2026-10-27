package com.pawth.model;

public class StaffProfile {
    private int userId;
    private String staffCustomId; // e.g., "PTH-2048"
    private String shelterName;   // e.g., "Pawth Rescue Center"
    private String position;      // e.g., "Senior Rescue Handler"
    private String shiftSchedule; // e.g., "Morning · 7 AM-3 PM"
    private int casesHandled;
    private int rescuesCount;

    public StaffProfile() {}

    public StaffProfile(int userId, String staffCustomId, String shelterName, String position, String shiftSchedule, int casesHandled, int rescuesCount) {
        this.userId = userId;
        this.staffCustomId = staffCustomId;
        this.shelterName = shelterName;
        this.position = position;
        this.shiftSchedule = shiftSchedule;
        this.casesHandled = casesHandled;
        this.rescuesCount = rescuesCount;
    }

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getStaffCustomId() { return staffCustomId; }
    public void setStaffCustomId(String staffCustomId) { this.staffCustomId = staffCustomId; }

    public String getShelterName() { return shelterName; }
    public void setShelterName(String shelterName) { this.shelterName = shelterName; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public String getShiftSchedule() { return shiftSchedule; }
    public void setShiftSchedule(String shiftSchedule) { this.shiftSchedule = shiftSchedule; }

    public int getCasesHandled() { return casesHandled; }
    public void setCasesHandled(int casesHandled) { this.casesHandled = casesHandled; }

    public int getRescuesCount() { return rescuesCount; }
    public void setRescuesCount(int rescuesCount) { this.rescuesCount = rescuesCount; }
}