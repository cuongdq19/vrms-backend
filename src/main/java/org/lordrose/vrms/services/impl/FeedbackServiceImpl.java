package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.Feedback;
import org.lordrose.vrms.repositories.FeedbackRepository;
import org.lordrose.vrms.services.FeedbackService;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public Double getAverageRating(Long providerId) {
        List<Feedback> feedbacks = feedbackRepository.findAllByRequestProviderId(providerId);
        return feedbacks.stream()
                .mapToDouble(Feedback::getRatings)
                .average()
                .orElse(-1);
    }
}
