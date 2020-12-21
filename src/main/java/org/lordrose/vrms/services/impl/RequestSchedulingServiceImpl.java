package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.Provider;
import org.lordrose.vrms.models.responses.SlotResponse;
import org.lordrose.vrms.repositories.ProviderRepository;
import org.lordrose.vrms.repositories.RequestRepository;
import org.lordrose.vrms.services.RequestSchedulingService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.lordrose.vrms.utils.DateTimeUtils.toLocalDateTime;
import static org.lordrose.vrms.utils.DateTimeUtils.toSeconds;

@RequiredArgsConstructor
@Service
public class RequestSchedulingServiceImpl implements RequestSchedulingService {

    private static final long HOUR_LIMIT = 2;

    private final ProviderRepository providerRepository;
    private final RequestRepository requestRepository;

    @Override
    public List<SlotResponse> getAvailableSlots(Long providerId, Long secondsOfDate) {
        LocalDate checkDate = toLocalDateTime(secondsOfDate).toLocalDate();
        Provider result = providerRepository.findById(providerId)
                .orElseThrow();
        List<LocalTime> times = getSlotsWithBeginningTime(result.getOpenTime(),
                result.getCloseTime(), result.getSlotDuration());
        return calculateSlotsStatus(checkDate, times, result.getSlotCapacity());
    }

    private List<LocalTime> getSlotsWithBeginningTime(LocalTime open, LocalTime close,
                                                      int slotDuration) {
        List<LocalTime> times = new ArrayList<>();
        final int openCloseDuration = close.toSecondOfDay()/60 - open.toSecondOfDay()/60;
        final int numberOfSlot = openCloseDuration/slotDuration;
        for (int i = 0; i < numberOfSlot; i++) {
            times.add(open);
            open = open.plusMinutes(slotDuration);
        }
        return times;
    }

    private List<SlotResponse> calculateSlotsStatus(final LocalDate dateToCheck,
                                            List<LocalTime> slotsBeginningList,
                                            int capacity) {
        if (dateToCheck.isBefore(LocalDate.now()))
            return slotsBeginningList.stream()
                    .map(slot -> SlotResponse.builder()
                            .beginTime(toSeconds(dateToCheck, slot))
                            .isUnavailable(true)
                            .build())
                    .collect(Collectors.toList());
        return slotsBeginningList.stream()
                .map(slot -> SlotResponse.builder()
                        .beginTime(toSeconds(dateToCheck, slot))
                        .isUnavailable(isFull(dateToCheck, slot, capacity) ||
                                isInThePass(dateToCheck, slot))
                        .build())
                .collect(Collectors.toList());
    }

    private boolean isInThePass(LocalDate date, LocalTime time) {
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        return dateTime.isBefore(LocalDateTime.now().plusHours(HOUR_LIMIT));
    }

    private boolean isFull(LocalDate date, LocalTime slotBeginning, int capacity) {
        LocalDateTime dateToCheck = LocalDateTime.of(date, slotBeginning);
        return requestRepository
                .countAllByBookingTimeEqualsAndStatus(dateToCheck, "ACCEPTED")
                >= capacity;
    }
}
