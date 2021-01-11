package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.Request;
import org.lordrose.vrms.domains.ServiceRequest;
import org.lordrose.vrms.domains.ServiceRequestPart;
import org.lordrose.vrms.domains.VehiclePart;
import org.lordrose.vrms.domains.constants.RequestStatus;
import org.lordrose.vrms.models.responses.PartSummaryResponse;
import org.lordrose.vrms.models.responses.ProviderMonthRevenueResponse;
import org.lordrose.vrms.models.responses.ProviderPartSummaryResponse;
import org.lordrose.vrms.models.responses.ProviderRequestSummaryResponse;
import org.lordrose.vrms.repositories.RequestRepository;
import org.lordrose.vrms.utils.DateTimeTuple;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static org.lordrose.vrms.converters.PartConverter.toEmptyModelPartResponse;
import static org.lordrose.vrms.utils.DateTimeUtils.getDateTimeTuples;

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

    public Object getRequestSummary(Long providerId, int yearValue) {
        List<DateTimeTuple> tuples = getDateTimeTuples(yearValue);

        return tuples.stream()
                .map(tuple -> ProviderRequestSummaryResponse.builder()
                        .month(tuple.from.getMonthValue())
                        .total(requestRepository.countAllByProviderIdAndBookingTimeBetween(
                                providerId, tuple.from, tuple.to))
                        .canceled(requestRepository.countAllByProviderIdAndStatusAndBookingTimeBetween(
                                providerId, RequestStatus.CANCELED, tuple.from, tuple.to))
                        .build())
                .sorted(Comparator.comparingInt(ProviderRequestSummaryResponse::getMonth))
                .collect(Collectors.toList());
    }

    @Transactional
    public Object getPartSummary(Long providerId, int yearValue) {
        List<DateTimeTuple> tuples = getDateTimeTuples(yearValue);

        return tuples.stream()
                .map(tuple -> {
                    List<Request> requests =
                            requestRepository.findAllByProviderIdAndStatusNotAndBookingTimeBetween(
                                    providerId, RequestStatus.CANCELED, tuple.from, tuple.to);
                    Map<VehiclePart, Double> partMap = requests.stream()
                            .flatMap(request -> request.getServices().stream()
                                    .flatMap(service -> service.getRequestParts().stream()))
                            .collect(groupingBy(ServiceRequestPart::getVehiclePart))
                            .entrySet().stream()
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    entry -> entry.getValue().stream()
                                            .mapToDouble(ServiceRequestPart::getQuantity)
                                            .sum()));

                    List<PartSummaryResponse> partSummaries = partMap.entrySet().stream()
                            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                            .limit(10)
                            .map(entry -> PartSummaryResponse.builder()
                                    .part(toEmptyModelPartResponse(entry.getKey()))
                                    .count(entry.getValue())
                                    .build())
                            .collect(Collectors.toList());

                    return ProviderPartSummaryResponse.builder()
                            .month(tuple.from.getMonthValue())
                            .partSummaries(partSummaries)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
