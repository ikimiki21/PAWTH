package com.pawth.service;

import com.pawth.model.RescueCase;

public class PriorityScoringEngine {

    public static void calculateAndSetPriority(RescueCase rc) {
        int score = 0;
        String flags = rc.getConditionFlags() != null ? rc.getConditionFlags().toLowerCase() : "";

        if (flags.contains("injured")) score += 5;
        if (flags.contains("busy road")) score += 4;
        if (flags.contains("aggressive") || flags.contains("distressed")) score += 3;
        if (flags.contains("near school")) score += 2;

        rc.setPriorityScore(score);

        if (score >= 9) {
            rc.setPriorityLevel(RescueCase.PriorityLevel.CRITICAL);
        } else if (score >= 6) {
            rc.setPriorityLevel(RescueCase.PriorityLevel.HIGH);
        } else if (score >= 3) {
            rc.setPriorityLevel(RescueCase.PriorityLevel.MEDIUM);
        } else {
            rc.setPriorityLevel(RescueCase.PriorityLevel.LOW);
        }
    }
}