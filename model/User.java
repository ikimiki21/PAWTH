package com.pawth.model;

import java.sql.Timestamp;

public class User {
    private int userId;
    private String email;
    private String passwordHash;
    private String fullName;
    private String phone;
    private String barangay;
    private Role role; // COMMUNITY or STAFF
    private Timestamp createdAt;

    // Optional associated profile objects
    private AdopterProfile adopterProfile;
    private StaffProfile staffProfile;

    public enum Role {
        COMMUNITY, STAFF
    }

    public User() {}

    public User(int userId, String email, String passwordHash, String fullName, String phone, String barangay, Role role) {
        this.userId = userId;
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.phone = phone;
        this.barangay = barangay;
        this.role = role;
    }

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getBarangay() { return barangay; }
    public void setBarangay(String barangay) { this.barangay = barangay; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public AdopterProfile getAdopterProfile() { return adopterProfile; }
    public void setAdopterProfile(AdopterProfile adopterProfile) { this.adopterProfile = adopterProfile; }

    public StaffProfile getStaffProfile() { return staffProfile; }
    public void setStaffProfile(StaffProfile staffProfile) { this.staffProfile = staffProfile; }
}