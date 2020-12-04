package org.lordrose.vrms.repositories;

import org.lordrose.vrms.domains.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findAllByRequestProviderId(Long providerId);
}
