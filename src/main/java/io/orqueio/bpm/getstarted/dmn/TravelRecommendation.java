package io.orqueio.bpm.getstarted.dmn;

import java.util.List;

public record TravelRecommendation(
    String destination,
    List<String> activities,
    String evaluationTime
) {
    public TravelRecommendation {
        if (destination == null || destination.isBlank()) {
            throw new IllegalArgumentException("Destination cannot be blank");
        }
        activities = List.copyOf(activities);
    }
}