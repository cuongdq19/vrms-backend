package org.lordrose.vrms.services;

import org.lordrose.vrms.models.responses.SlotResponse;

import java.util.List;

public interface RequestSchedulingService {

    List<SlotResponse> getAvailableSlots(Long providerId, Long date);
}
