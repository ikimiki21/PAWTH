package com.pawth.service;

import com.pawth.model.AdopterProfile;
import com.pawth.model.Animal;

public class MatchingEngine {

    public static int calculateMatchPercentage(AdopterProfile profile, Animal animal) {
        if (profile == null || animal == null) return 75; // Default fallback score

        int match = 70; // Base score

        // Home space check
        if ("House with yard".equalsIgnoreCase(profile.getHomeType())) {
            match += 15;
        } else if ("Apartment".equalsIgnoreCase(profile.getHomeType()) && "Small".equalsIgnoreCase(animal.getSpecies())) {
            match += 10;
        }

        // Traits check
        String traits = animal.getPersonalityTraits() != null ? animal.getPersonalityTraits().toLowerCase() : "";
        if (profile.isHasChildren() && traits.contains("people-friendly")) {
            match += 10;
        }

        return Math.min(match, 98); // Max cap 98%
    }
}