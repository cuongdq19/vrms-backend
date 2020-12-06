package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.Feedback;
import org.lordrose.vrms.models.responses.FeedbackResponse;

import java.util.List;
import java.util.stream.Collectors;

public class FeedbackConverter {

    public static FeedbackResponse toFeedbackResponse(Feedback feedback) {
        return FeedbackResponse.builder()
                .build();
    }

    public static List<FeedbackResponse> toFeedbackResponses(List<Feedback> feedbacks) {
        return feedbacks.stream()
                .map(FeedbackConverter::toFeedbackResponse)
                .collect(Collectors.toList());
    }
}
