package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.Feedback;
import org.lordrose.vrms.models.responses.FeedbackHistoryResponse;
import org.lordrose.vrms.models.responses.FeedbackResponse;

import java.util.List;
import java.util.stream.Collectors;

import static org.lordrose.vrms.utils.FileUrlUtils.getUrlsAsArray;

public class FeedbackConverter {

    public static FeedbackResponse toFeedbackResponse(Feedback feedback) {
        return FeedbackResponse.builder()
                .id(feedback.getId())
                .ratings(feedback.getRatings())
                .content(feedback.getContent())
                .imageUrls(feedback.getImageUrls())
                .userId(feedback.getUser().getId())
                .fullName(feedback.getUser().getFullName())
                .userImageUrl(feedback.getUser().getImageUrl())
                .requestId(feedback.getRequest().getId())
                .build();
    }

    public static List<FeedbackResponse> toFeedbackResponses(List<Feedback> feedbacks) {
        return feedbacks.stream()
                .map(FeedbackConverter::toFeedbackResponse)
                .collect(Collectors.toList());
    }

    public static FeedbackHistoryResponse toFeedbackHistoryResponse(Feedback feedback) {
        return FeedbackHistoryResponse.builder()
                .id(feedback.getId())
                .ratings(feedback.getRatings())
                .content(feedback.getContent())
                .imageUrls(getUrlsAsArray(feedback.getImageUrls()))
                .build();
    }
}
