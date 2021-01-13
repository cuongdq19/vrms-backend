package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.converters.UserConverter;
import org.lordrose.vrms.domains.Provider;
import org.lordrose.vrms.domains.Request;
import org.lordrose.vrms.domains.User;
import org.lordrose.vrms.models.requests.ProviderRequest;
import org.lordrose.vrms.models.responses.FeedbackResponse;
import org.lordrose.vrms.models.responses.ProviderDetailResponse;
import org.lordrose.vrms.models.responses.ProviderDistanceResponse;
import org.lordrose.vrms.models.responses.TechnicianResponse;
import org.lordrose.vrms.repositories.FeedbackRepository;
import org.lordrose.vrms.repositories.ManufacturerRepository;
import org.lordrose.vrms.repositories.ProviderRepository;
import org.lordrose.vrms.repositories.RequestRepository;
import org.lordrose.vrms.repositories.UserRepository;
import org.lordrose.vrms.services.FeedbackService;
import org.lordrose.vrms.services.ProviderService;
import org.lordrose.vrms.utils.distances.GeoPoint;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.lordrose.vrms.converters.FeedbackConverter.toFeedbackResponses;
import static org.lordrose.vrms.converters.ProviderConverter.toProviderDetailResponse;
import static org.lordrose.vrms.converters.ProviderConverter.toProviderDetailResponses;
import static org.lordrose.vrms.exceptions.ResourceNotFoundException.newExceptionWithId;
import static org.lordrose.vrms.utils.DateTimeUtils.toLocalDateTime;
import static org.lordrose.vrms.utils.DateTimeUtils.toLocalTime;
import static org.lordrose.vrms.utils.FileUrlUtils.getUrlsAsArray;
import static org.lordrose.vrms.utils.distances.DistanceCalculator.calculate;

@RequiredArgsConstructor
@Service
public class ProviderServiceImpl implements ProviderService {

    private final ProviderRepository providerRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final FeedbackService feedbackService;

    @Override
    public List<ProviderDetailResponse> findAll() {
        return toProviderDetailResponses(providerRepository.findAll());
    }

    @Override
    public List<ProviderDistanceResponse> findAll(GeoPoint currentPos) {
        List<Provider> providers = providerRepository.findAll();

        return providers.stream()
                .map(provider -> ProviderDistanceResponse.builder()
                        .id(provider.getId())
                        .name(provider.getName())
                        .address(provider.getAddress())
                        .imageUrls(getUrlsAsArray(provider.getImageUrls()))
                        .openTime(provider.getOpenTime().toString())
                        .closeTime(provider.getCloseTime().toString())
                        .ratings(feedbackService.getAverageRating(provider.getId()))
                        .distance(calculate(currentPos, GeoPoint.builder()
                                .latitude(provider.getLatitude())
                                .longitude(provider.getLongitude())
                                .build()))
                        .build())
                .sorted(Comparator.comparingDouble(ProviderDistanceResponse::getDistance))
                .collect(Collectors.toList());
    }

    @Override
    public ProviderDetailResponse update(Long id, ProviderRequest request) {
        Provider result = providerRepository.findById(id)
                .orElseThrow(() -> newExceptionWithId(id));

        result.setName(request.getProviderName());
        result.setAddress(request.getAddress());
        result.setLatitude(request.getLatitude());
        result.setLongitude(request.getLongitude());
        result.setOpenTime(toLocalTime(request.getOpenTime()));
        result.setCloseTime(toLocalTime(request.getCloseTime()));
        result.setSlotDuration(request.getSlotDuration());
        result.setSlotCapacity(request.getSlotCapacity());

        return toProviderDetailResponse(providerRepository.save(result));
    }

    @Override
    public List<FeedbackResponse> findAllFeedbackByProviderId(Long providerId) {
        return toFeedbackResponses(feedbackRepository.findAllByRequestProviderId(providerId));

    }

    @Override
    public List<TechnicianResponse> findAvailableTechnician(Long providerId, Long time) {
        List<User> providerUsers = userRepository.findAllByProviderIdAndRoleName(providerId,
                "TECHNICIAN").stream()
                .filter(User::getIsActive)
                .collect(Collectors.toList());
        List<Request> requests = requestRepository.findAllByProviderIdAndBookingTime(providerId,
                        toLocalDateTime(time));
        List<Long> unavailableUserIds = requests.stream()
                .map(request -> request.getTechnician() == null ? null
                        : request.getTechnician().getId())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return providerUsers.stream()
                .filter(user -> !unavailableUserIds.contains(user.getId()))
                .map(UserConverter::toTechnicianResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Object getRatingByProvider(Long providerId) {
        return feedbackService.getAverageRating(providerId);
    }
}
