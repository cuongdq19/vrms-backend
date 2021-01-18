package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.Provider;
import org.lordrose.vrms.domains.Request;
import org.lordrose.vrms.domains.ServiceRequest;
import org.lordrose.vrms.domains.ServiceRequestPart;
import org.lordrose.vrms.domains.VehiclePart;
import org.lordrose.vrms.domains.constants.RequestStatus;
import org.lordrose.vrms.models.responses.PartSummaryResponse;
import org.lordrose.vrms.models.responses.ProviderMonthRevenueResponse;
import org.lordrose.vrms.models.responses.ProviderPartSummaryResponse;
import org.lordrose.vrms.models.responses.ProviderRequestSummaryResponse;
import org.lordrose.vrms.models.responses.ProviderSummaryResponse;
import org.lordrose.vrms.models.responses.RequestRatioResponse;
import org.lordrose.vrms.models.responses.RevenueDetailResponse;
import org.lordrose.vrms.repositories.ProviderRepository;
import org.lordrose.vrms.repositories.RequestRepository;
import org.lordrose.vrms.repositories.RoleRepository;
import org.lordrose.vrms.repositories.UserRepository;
import org.lordrose.vrms.utils.DateTimeTuple;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static org.lordrose.vrms.converters.PartConverter.toEmptyModelPartResponse;
import static org.lordrose.vrms.exceptions.ResourceNotFoundException.newExceptionWithValue;
import static org.lordrose.vrms.utils.DateTimeUtils.getDateTimeTuples;
import static org.lordrose.vrms.utils.FileUrlUtils.getUrlsAsArray;

@RequiredArgsConstructor
@Service
public class ChartServiceImpl {

    private final RequestRepository requestRepository;
    private final ProviderRepository providerRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final FeedbackServiceImpl feedbackService;

    public Object getRevenueByProvider(Long providerId, int yearValue) {
        List<DateTimeTuple> tuples = getDateTimeTuples(yearValue);

        return tuples.stream()
                .map(tuple -> {
                    List<Request> requests = requestRepository.findAllByProviderIdAndStatusAndCheckoutTimeBetween(
                            providerId, RequestStatus.FINISHED, tuple.from, tuple.to);

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

                    return ProviderMonthRevenueResponse.builder()
                            .month(tuple.from.getMonthValue())
                            .services(serviceTotal)
                            .parts(partTotal)
                            .total(serviceTotal + partTotal)
                            .build();
                })
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

    public Object getRatingsSummary(int yearValue) {
        List<DateTimeTuple> tuples = getDateTimeTuples(yearValue);
        List<Provider> providers = providerRepository.findAll();
        List<ProviderSummaryResponse> responses = new ArrayList<>();

        providers.forEach(provider -> {
            ProviderSummaryResponse response = ProviderSummaryResponse.builder()
                    .id(provider.getId())
                    .providerName(provider.getName())
                    .address(provider.getAddress())
                    .latitude(provider.getLatitude())
                    .longitude(provider.getLongitude())
                    .openTime(provider.getOpenTime().toString())
                    .closeTime(provider.getCloseTime().toString())
                    .imageUrls(getUrlsAsArray(provider.getImageUrls()))
                    .contractPhoneNumber(provider.getContract().getPhoneNumber())
                    .contractEmail(provider.getContract().getEmail())
                    .ratings(feedbackService.getAverageRating(provider.getId()))
                    .revenues(new ArrayList<>())
                    .build();
            tuples.forEach(tuple -> {
                List<Request> requests = requestRepository.findAllByProviderIdAndStatusAndBookingTimeBetween(
                        provider.getId(), RequestStatus.FINISHED, tuple.from, tuple.to);
                response.getRevenues().add(RevenueDetailResponse.builder()
                        .month(tuple.from.getMonthValue())
                        .totalRevenue(requests.stream()
                                .mapToDouble(request -> request.getServices().stream()
                                        .filter(ServiceRequest::getIsActive)
                                        .mapToDouble(service ->
                                                service.getPrice() + service.getRequestParts().stream()
                                                        .mapToDouble(part -> part.getPrice() * part.getQuantity())
                                                        .sum())
                                        .sum())
                                .sum())
                        .incurredRevenue(requests.stream()
                                .mapToDouble(request -> request.getServices().stream()
                                        .filter(ServiceRequest::getIsActive)
                                        .filter(ServiceRequest::getIsIncurred)
                                        .mapToDouble(service ->
                                                service.getPrice() + service.getRequestParts().stream()
                                                        .mapToDouble(part -> part.getPrice() * part.getQuantity())
                                                        .sum())
                                        .sum())
                                .sum())
                        .build());
            });

            responses.add(response);
        });

        return responses.stream()
                .sorted(Comparator.comparingDouble(ProviderSummaryResponse::getRatings).reversed())
                .collect(Collectors.toList());
    }

    public Object getNewProviderSummary() {
        List<DateTimeTuple> tuples = getDateTimeTuples(Year.now().getValue());

        return tuples.stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.from.getMonthValue(),
                        tuple -> providerRepository.countAllByCreateAtBetween(tuple.from, tuple.to)));
    }

    public Object getNewCustomers() {
        List<DateTimeTuple> tuples = getDateTimeTuples(Year.now().getValue());

        return tuples.stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.from.getMonthValue(),
                        tuple -> userRepository.countAllByRoleAndCreateAtBetween(
                                roleRepository.findByNameIgnoreCase("USER")
                                        .orElseThrow(() -> newExceptionWithValue("USER")),
                                tuple.from, tuple.to)));
    }

    public Object getNewRequests() {
        List<DateTimeTuple> tuples = getDateTimeTuples(Year.now().getValue());

        return tuples.stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.from.getMonthValue(),
                        tuple -> requestRepository.countAllByCreateAtBetween(tuple.from, tuple.to)));
    }

    public Object getNewRequestRatios() {
        List<DateTimeTuple> tuples = getDateTimeTuples(Year.now().getValue());

        return tuples.stream()
                .map(tuple -> RequestRatioResponse.builder()
                        .month(tuple.from.getMonthValue())
                        .totalRequest(requestRepository.countAllByCreateAtBetween(
                                tuple.from,
                                tuple.to))
                        .canceledRequest(requestRepository.countAllByStatusAndCreateAtBetween(
                                RequestStatus.CANCELED,
                                tuple.from,
                                tuple.to))
                        .build())
                .sorted(Comparator.comparingInt(RequestRatioResponse::getMonth))
                .collect(Collectors.toList());
    }
}
