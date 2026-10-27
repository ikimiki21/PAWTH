package com.pawth.view.components;

import com.pawth.dao.*;
import com.pawth.model.*;
import com.pawth.service.*;
import java.util.*;

public record WorkspaceData(
        List<Animal> animals,
        List<RescueCase> cases,
        List<AdoptionApplication> applications,
        List<Notification> notifications,
        Map<String, Integer> hotspots) {
    public static WorkspaceData load(User user) {
        boolean staff = user.getRole() == User.Role.STAFF;
        List<Animal> animals = new AnimalDAO().getAllAnimals();
        // Temporary UI placeholder: keep only the seeded Buddy (ID 1).
        // Other animals remain stored in MySQL; remove this filter to restore the full catalogue.
        animals.removeIf(animal -> animal.getAnimalId() != 1);
        animals.sort(Comparator.comparingInt(Animal::getAnimalId));
        for (Animal a : animals)
            a.setMatchPercentage(
                    MatchingEngine.calculateMatchPercentage(user.getAdopterProfile(), a));
        List<RescueCase> cases = new RescueCaseDAO().getAllActiveCases();
        if (!staff)
            cases = cases.stream().filter(r -> r.getReporterId() == user.getUserId()).toList();
        return new WorkspaceData(
                animals,
                cases,
                staff
                        ? new AdoptionApplicationDAO()
                                .getPendingQueue().stream()
                                        .filter(application -> application.getAnimalId() == 1)
                                        .toList()
                        : List.of(),
                new NotificationDAO().getUserNotifications(user.getUserId()),
                staff ? new AnalyticsService().getHotspotCountsByBarangay() : Map.of());
    }
}
