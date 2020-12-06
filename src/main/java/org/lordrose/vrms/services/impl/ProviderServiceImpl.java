package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.converters.TechnicianConverter;
import org.lordrose.vrms.domains.Manufacturer;
import org.lordrose.vrms.domains.Provider;
import org.lordrose.vrms.domains.Request;
import org.lordrose.vrms.domains.User;
import org.lordrose.vrms.models.requests.ProviderRequest;
import org.lordrose.vrms.models.responses.FeedbackResponse;
import org.lordrose.vrms.models.responses.ProviderDetailResponse;
import org.lordrose.vrms.models.responses.TechnicianResponse;
import org.lordrose.vrms.repositories.FeedbackRepository;
import org.lordrose.vrms.repositories.ManufacturerRepository;
import org.lordrose.vrms.repositories.ProviderRepository;
import org.lordrose.vrms.repositories.RequestRepository;
import org.lordrose.vrms.repositories.UserRepository;
import org.lordrose.vrms.services.ProviderService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.lordrose.vrms.converters.FeedbackConverter.toFeedbackResponses;
import static org.lordrose.vrms.converters.ProviderConverter.toProviderDetailResponse;
import static org.lordrose.vrms.converters.ProviderConverter.toProviderDetailResponses;
import static org.lordrose.vrms.exceptions.ResourceNotFoundException.newExceptionWithId;
import static org.lordrose.vrms.utils.DateTimeUtils.toLocalDateTime;
import static org.lordrose.vrms.utils.DateTimeUtils.toLocalTime;

@RequiredArgsConstructor
@Service
public class ProviderServiceImpl implements ProviderService {

    private final ProviderRepository providerRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    @Override
    public List<ProviderDetailResponse> findAll() {
        return toProviderDetailResponses(providerRepository.findAll());
    }

    @Override
    public ProviderDetailResponse update(Long id, ProviderRequest request) {
        Provider result = providerRepository.findById(id)
                .orElseThrow(() -> newExceptionWithId(id.toString()));
        Manufacturer manufacturer = manufacturerRepository.findById(request.getManufacturerId())
                .orElse(null);

        result.setName(request.getProviderName());
        result.setAddress(request.getAddress());
        result.setLatitude(request.getLatitude());
        result.setLongitude(request.getLongitude());
        result.setOpenTime(toLocalTime(request.getOpenTime()));
        result.setCloseTime(toLocalTime(request.getCloseTime()));
        result.setSlotDuration(request.getSlotDuration());
        result.setSlotCapacity(request.getSlotCapacity());
        result.setManufacturer(manufacturer);

        return toProviderDetailResponse(providerRepository.save(result));
    }

    @Override
    public List<FeedbackResponse> findAllFeedbackByProviderId(Long providerId) {
        return toFeedbackResponses(feedbackRepository.findAllByRequestProviderId(providerId));

    }

    @Override
    public List<TechnicianResponse> findAvailableTechnician(Long providerId, Long time) {
        List<User> providerUsers = userRepository.findAllByProviderIdAndRoleName(providerId,
                "TECHNICIAN");
        List<Request> requests = requestRepository.findAllByProviderIdAndBookingTime(providerId,
                        toLocalDateTime(time));
        List<Long> unavailableUserIds = requests.stream()
                .map(request -> request.getTechnician() == null ? null
                        : request.getTechnician().getId())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return providerUsers.stream()
                .filter(user -> !unavailableUserIds.contains(user.getId()))
                .map(TechnicianConverter::toTechnicianResponse)
                .collect(Collectors.toList());
    }
}
