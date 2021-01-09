package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.Request;
import org.lordrose.vrms.domains.ServiceRequest;
import org.lordrose.vrms.models.responses.ProviderMonthRevenueResponse;
import org.lordrose.vrms.repositories.RequestRepository;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@RequiredArgsConstructor
@Service
public class ProviderChartServiceImpl {

    private final RequestRepository requestRepository;

    public Object getRevenueByProvider(Long providerId) {
        List<Month> months = Arrays.asList(Month.values());
        List<ProviderMonthRevenueResponse> responses = new ArrayList<>();
        Map<Month, List<Request>> resultMap = months.stream()
                .collect(Collectors.toMap(month -> month, month -> Collections.emptyList()));

        resultMap.putAll(
                requestRepository.findAllByProviderIdAndCheckoutTimeNotNull(providerId).stream()
                        .collect(groupingBy(request -> request.getCheckoutTime().getMonth())));

        resultMap.forEach(((month, requests) -> {
            Double serviceTotal = requests.stream()
                    .mapToDouble(request -> request.getServices().stream()
                            .mapToDouble(ServiceRequest::getPrice)
                            .sum())
                    .sum();
            Double partTotal = requests.stream()
                    .mapToDouble(request -> request.getServices().stream()
                            .mapToDouble(service -> service.getRequestParts().stream()
                                    .mapToDouble(part -> part.getQuantity() * part.getPrice())
                                    .sum())
                            .sum())
                    .sum();
            responses.add(ProviderMonthRevenueResponse.builder()
                    .month(month.getValue())
                    .services(serviceTotal)
                    .parts(partTotal)
                    .total(serviceTotal + partTotal)
                    .build());
        }));

        return responses.stream()
                .sorted(Comparator.comparingInt(ProviderMonthRevenueResponse::getMonth))
                .collect(Collectors.toList());
    }
}
