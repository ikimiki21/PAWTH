-- ========================================================
-- PAWTH DATABASE INITIALIZATION SCRIPT
-- System: Stray Animal Rescue Management and Adoption System
-- ========================================================

DROP DATABASE IF EXISTS pawth_db;
CREATE DATABASE pawth_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE pawth_db;

-- --------------------------------------------------------
-- 1. USERS TABLE
-- Stores credentials and primary role (COMMUNITY or STAFF)
-- --------------------------------------------------------
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    barangay VARCHAR(100),
    role ENUM('COMMUNITY', 'STAFF') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- --------------------------------------------------------
-- 2. ADOPTER PROFILES TABLE
-- Specific attributes for Community / Adopter workspace
-- --------------------------------------------------------
CREATE TABLE adopter_profiles (
    user_id INT PRIMARY KEY,
    home_type VARCHAR(100) DEFAULT 'House with yard',
    has_children BOOLEAN DEFAULT FALSE,
    has_other_pets BOOLEAN DEFAULT FALSE,
    preferred_animal VARCHAR(100) DEFAULT 'Dog · Medium',
    pet_experience TEXT,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- --------------------------------------------------------
-- 3. STAFF PROFILES TABLE
-- Specific attributes for Shelter Operations workspace
-- --------------------------------------------------------
CREATE TABLE staff_profiles (
    user_id INT PRIMARY KEY,
    staff_custom_id VARCHAR(50) NOT NULL UNIQUE,
    shelter_name VARCHAR(100) DEFAULT 'Pawth Rescue Center',
    position VARCHAR(100) DEFAULT 'Senior Rescue Handler',
    shift_schedule VARCHAR(50) DEFAULT 'Morning · 7 AM–3 PM',
    cases_handled INT DEFAULT 0,
    rescues_count INT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- --------------------------------------------------------
-- 4. ANIMAL LISTINGS TABLE
-- Shelter inventory and pets ready for adoption
-- --------------------------------------------------------
CREATE TABLE animals (
    animal_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    species VARCHAR(50) NOT NULL,
    breed VARCHAR(100),
    age_years INT,
    gender ENUM('Male', 'Female') DEFAULT 'Male',
    health_status VARCHAR(150),
    visit_schedule VARCHAR(100),
    adoption_status ENUM('AVAILABLE', 'ON_HOLD', 'MEDICAL_CARE', 'ADOPTED') DEFAULT 'AVAILABLE',
    photo_url VARCHAR(255),
    personality_traits VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- --------------------------------------------------------
-- 5. RESCUE CASES TABLE
-- Reported stray animals & operational tracking queue
-- --------------------------------------------------------
CREATE TABLE rescue_cases (
    case_id INT PRIMARY KEY AUTO_INCREMENT,
    reporter_id INT NOT NULL,
    animal_type VARCHAR(50) NOT NULL,
    approximate_size VARCHAR(50) DEFAULT 'Small',
    location_landmark VARCHAR(255),
    barangay VARCHAR(100) NOT NULL,
    area_street VARCHAR(255),
    photo_url VARCHAR(255),
    condition_flags VARCHAR(255),
    additional_notes TEXT,
    priority_level ENUM('CRITICAL', 'HIGH', 'MEDIUM', 'LOW') DEFAULT 'MEDIUM',
    priority_score INT DEFAULT 6,
    case_status ENUM('REPORTED', 'VERIFIED', 'RESCUE_ASSIGNED', 'RESCUED') DEFAULT 'REPORTED',
    assigned_handler_id INT NULL,
    handler_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (reporter_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (assigned_handler_id) REFERENCES users(user_id) ON DELETE SET NULL
);

-- --------------------------------------------------------
-- 6. ADOPTION APPLICATIONS TABLE
-- Community adoption submissions
-- --------------------------------------------------------
CREATE TABLE adoption_applications (
    application_id INT PRIMARY KEY AUTO_INCREMENT,
    applicant_id INT NOT NULL,
    animal_id INT NOT NULL,
    match_percentage INT DEFAULT 85,
    application_status ENUM('NEW', 'CONSULTATION', 'HOME_CHECK', 'APPROVED', 'REJECTED') DEFAULT 'NEW',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (applicant_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (animal_id) REFERENCES animals(animal_id) ON DELETE CASCADE
);

-- --------------------------------------------------------
-- 7. NOTIFICATIONS TABLE
-- User and staff dynamic alerts
-- --------------------------------------------------------
CREATE TABLE notifications (
    notification_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    message TEXT NOT NULL,
    category ENUM('RESCUE', 'ADOPTION', 'GENERAL') DEFAULT 'GENERAL',
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);


-- ========================================================
-- SEED DATA (POPULATING DEMO ACCOUNTS & SAMPLE DATA)
-- ========================================================

-- 1. Insert Demo Users (Adopter & Staff)
-- Note: Credentials match UI prototype samples
INSERT INTO users (user_id, email, password_hash, full_name, phone, barangay, role) VALUES
(1, 'user@gmail.com', 'password123', 'Argon Metal', '+63 998 765 4321', 'Barangay Maligaya', 'COMMUNITY'),
(2, 'shelter.staff@gmail.com', 'staff123', 'Maria Santos', '+63 912 345 6789', 'Barangay Maligaya', 'STAFF');

-- 2. Insert Profile Details
INSERT INTO adopter_profiles (user_id, home_type, has_children, has_other_pets, preferred_animal, pet_experience) VALUES
(1, 'House with yard', FALSE, FALSE, 'Dog · Medium', 'Experienced dog owner for 3 years.');

INSERT INTO staff_profiles (user_id, staff_custom_id, shelter_name, position, shift_schedule, cases_handled, rescues_count) VALUES
(2, 'PTH-2048', 'Pawth Rescue Center', 'Senior Rescue Handler', 'Morning · 7 AM–3 PM', 48, 31);

-- 3. Insert Animal Records (Matching UI prototype gallery)
INSERT INTO animals (animal_id, name, species, breed, age_years, gender, health_status, visit_schedule, adoption_status, photo_url, personality_traits) VALUES
(1, 'Buddy', 'Dog', 'Aspen mix', 2, 'Male', 'Healthy · Fully Vaccinated', 'Sat, 10:00 AM', 'AVAILABLE', 'assets/pet_photos/buddy.jpg', 'Playful, People-friendly'),
(2, 'Luna', 'Cat', 'Domestic shorthair', 1, 'Female', 'Healthy · Spayed', 'Fri, 2:00 PM', 'AVAILABLE', 'assets/pet_photos/luna.jpg', 'Gentle, Quiet home'),
(3, 'Milo', 'Dog', 'Terrier mix', 3, 'Male', 'Healthy · Fully Vaccinated', 'Sun, 11:30 AM', 'AVAILABLE', 'assets/pet_photos/milo.jpg', 'Curious, Cuddly'),
(4, 'Nori', 'Dog', 'Labrador mix', 4, 'Female', 'Recovered · Vaccinated', 'Sat, 3:00 PM', 'ON_HOLD', 'assets/pet_photos/nori.jpg', 'Calm, Good with children'),
(5, 'Pepper', 'Dog', 'Shepherd mix', 2, 'Female', 'Under Treatment · Vaccinated', 'Tue, 1:00 PM', 'MEDICAL_CARE', 'assets/pet_photos/pepper.jpg', 'Active, Quick learner'),
(6, 'Max', 'Dog', 'Mixed breed', 5, 'Male', 'Healthy · Vaccinated', 'Wed, 10:30 AM', 'ADOPTED', 'assets/pet_photos/max.jpg', 'Loyal, Experienced owner');

-- 4. Insert Sample Rescue Cases (Matching UI prototype)
INSERT INTO rescue_cases (case_id, reporter_id, animal_type, approximate_size, location_landmark, barangay, area_street, condition_flags, priority_level, priority_score, case_status, assigned_handler_id) VALUES
(102, 1, 'Dog', 'Medium', 'Maharlika Hwy', 'Barangay Maligaya', 'near busy road', 'Injured, Near busy road', 'CRITICAL', 12, 'RESCUE_ASSIGNED', 2),
(108, 1, 'Cat', 'Small', 'Bagong Silang School', 'Barangay Maligaya', 'near school', 'Distressed, Near school', 'HIGH', 9, 'VERIFIED', NULL),
(110, 1, 'Dog', 'Small', 'Rizal Avenue', 'Barangay Maligaya', 'main avenue', 'Repeated report', 'MEDIUM', 6, 'REPORTED', NULL);

-- 5. Insert Sample Adoption Application
INSERT INTO adoption_applications (application_id, applicant_id, animal_id, match_percentage, application_status) VALUES
(1, 1, 1, 94, 'NEW');

-- 6. Insert Sample Notifications
INSERT INTO notifications (user_id, title, message, category) VALUES
(1, 'Your report has been verified', 'A handler reviewed report #108 and updated its priority.', 'RESCUE'),
(1, 'Buddy is available Saturday', 'A new visitation slot opened at 10:00 AM.', 'ADOPTION'),
(2, 'Critical rescue report #114', 'Injured dog reported near Commonwealth Road.', 'RESCUE');