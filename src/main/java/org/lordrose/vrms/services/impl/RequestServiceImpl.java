package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.Feedback;
import org.lordrose.vrms.domains.PartRequest;
import org.lordrose.vrms.domains.Provider;
import org.lordrose.vrms.domains.Request;
import org.lordrose.vrms.domains.ServiceRequest;
import org.lordrose.vrms.domains.ServiceRequestPart;
import org.lordrose.vrms.domains.Vehicle;
import org.lordrose.vrms.domains.VehiclePart;
import org.lordrose.vrms.exceptions.InvalidArgumentException;
import org.lordrose.vrms.models.requests.CheckinRequest;
import org.lordrose.vrms.models.requests.FeedbackRequest;
import org.lordrose.vrms.models.requests.RequestInfoRequest;
import org.lordrose.vrms.models.requests.ServicePartRequest;
import org.lordrose.vrms.models.responses.FeedbackResponse;
import org.lordrose.vrms.models.responses.RequestCheckOutResponse;
import org.lordrose.vrms.models.responses.RequestHistoryDetailResponse;
import org.lordrose.vrms.repositories.FeedbackRepository;
import org.lordrose.vrms.repositories.NotificationRepository;
import org.lordrose.vrms.repositories.PackageRequestRepository;
import org.lordrose.vrms.repositories.PartRepository;
import org.lordrose.vrms.repositories.PartRequestRepository;
import org.lordrose.vrms.repositories.ProviderRepository;
import org.lordrose.vrms.repositories.RequestRepository;
import org.lordrose.vrms.repositories.ServicePackageRepository;
import org.lordrose.vrms.repositories.ServiceRepository;
import org.lordrose.vrms.repositories.ServiceRequestPartRepository;
import org.lordrose.vrms.repositories.ServiceRequestRepository;
import org.lordrose.vrms.repositories.UserRepository;
import org.lordrose.vrms.repositories.VehicleRepository;
import org.lordrose.vrms.services.RequestService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.lordrose.vrms.converters.FeedbackConverter.toFeedbackResponse;
import static org.lordrose.vrms.converters.RequestConverter.toRequestCheckoutResponse;
import static org.lordrose.vrms.converters.RequestConverter.toRequestCheckoutResponses;
import static org.lordrose.vrms.converters.RequestConverter.toRequestHistoryDetailResponses;
import static org.lordrose.vrms.exceptions.ResourceNotFoundException.newExceptionWithId;
import static org.lordrose.vrms.utils.DateTimeUtils.toLocalDateTime;

@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class RequestServiceImpl implements RequestService {
    
    private final RequestRepository requestRepository;
    private final ProviderRepository providerRepository;
    private final VehicleRepository vehicleRepository;
    private final ServiceRepository serviceRepository;
    private final ServiceRequestRepository serviceRequestRepository;
    private final PartRepository partRepository;
    private final PartRequestRepository partRequestRepository;
    private final ServicePackageRepository servicePackageRepository;
    private final PackageRequestRepository packageRequestRepository;
    private final NotificationRepository notificationRepository;
    private final ServiceRequestPartRepository requestPartRepository;
    private final UserRepository userRepository;
    private final FeedbackRepository feedbackRepository;
    
    @Override
    public List<RequestHistoryDetailResponse> findAllByUserId(Long userId) {
        return toRequestHistoryDetailResponses(requestRepository.findAllByVehicle_User_Id(userId));
    }

    @Override
    public List<RequestHistoryDetailResponse> findAllByVehicleId(Long vehicleId) {
        return toRequestHistoryDetailResponses(requestRepository.findAllByVehicleId(vehicleId));
    }

    @Override
    public List<RequestCheckOutResponse> findAllByProviderId(Long providerId) {
        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> newExceptionWithId(providerId));
        return toRequestCheckoutResponses(requestRepository.findAllByProviderOrderByBookingTimeDesc(provider));
    }

    @Override
    public RequestCheckOutResponse findRequestById(Long requestId) {
        Request result = requestRepository.findById(requestId)
                .orElseThrow(() -> newExceptionWithId(requestId));
        return toRequestCheckoutResponse(result);
    }

    @Override
    public RequestCheckOutResponse create(RequestInfoRequest request) {
        Set<Long> packageIds = request.getPackageIds();
        Map<Long, ServicePartRequest> serviceParts = request.getServiceParts();
        Map<Long, Integer> partMap = request.getParts();
        if (packageIds.isEmpty() && serviceParts.keySet().isEmpty() && partMap.keySet().isEmpty()) {
            throw new InvalidArgumentException("At least one field is required!");
        }

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> newExceptionWithId(request.getVehicleId()));
        Provider provider = providerRepository.findById(request.getProviderId())
                .orElseThrow(() -> newExceptionWithId(request.getProviderId()));

        Request saved = requestRepository.save(Request.builder()
                .bookingTime(toLocalDateTime(request.getBookingTime()))
                .note(request.getNote())
                .imageUrls("")
                .status("ACCEPTED")
                .vehicle(vehicle)
                .provider(provider)
                .build());

        /*notificationRepository.save(org.lordrose.vrms.domains.Notification.builder()
                .title("Your booking will start today at " +
                        saved.getBookingTime().getHour() + "h" + saved.getBookingTime().getMinute())
                .content("Content")
                .user(vehicle.getUser())
                .notifyAt(saved.getBookingTime().minusHours(2))
                .isActive(true)
                .build());*/

        /*Set<PackageRequest> packages = servicePackageRepository.findAllById(packageIds).stream()
                .map(servicePackage -> PackageRequest.builder()
                        .servicePackage(servicePackage)
                        .services(servicePackage.getServices().stream()
                                .map(serviceDetail -> ServiceRequest.builder()
                                        .price(serviceDetail.getPrice())
                                        .service(serviceDetail)
                                        .request(saved)
                                        .build())
                                .collect(Collectors.toSet()))
                        .request(saved)
                        .build())
                .collect(Collectors.toSet());
        saved.setPackages(new HashSet<>(packageRequestRepository.saveAll(packages)));*/

        Set<ServiceRequest> services = serviceRepository.findAllById(serviceParts.keySet()).stream()
                .map(serviceDetail -> {
                    ServicePartRequest partRequest = request.getServiceParts().get(serviceDetail.getId());
                    Integer quantity = partRequest.getQuantity();
                    VehiclePart part = partRepository.findById(partRequest.getId())
                            .orElseThrow(() -> newExceptionWithId(partRequest.getId()));
                    ServiceRequest result = serviceRequestRepository.save(ServiceRequest.builder()
                            .price(serviceDetail.getPrice())
                            .service(serviceDetail)
                            .request(saved)
                            .requestPart(requestPartRepository.save(ServiceRequestPart.builder()
                                    .quantity(quantity)
                                    .price(part.getPrice())
                                    .vehiclePart(part)
                                    .build()))
                            .build());

                    return serviceRequestRepository.save(result);
                })
                .collect(Collectors.toSet());
        saved.setServices(services);

        Set<PartRequest> parts = partRepository.findAllById(partMap.keySet()).stream()
                .map(part -> PartRequest.builder()
                        .quantity(partMap.get(part.getId()))
                        .price(part.getPrice())
                        .request(saved)
                        .vehiclePart(part)
                        .build())
                .collect(Collectors.toSet());
        saved.setParts(new HashSet<>(partRequestRepository.saveAll(parts)));

        /*Message content = Message.builder()
                .setTopic("ProviderId_" + saved.getProvider().getId())
                .setNotification(Notification.builder()
                        .setTitle("Incoming booking request")
                        .setBody("A new booking request is received with id: " + saved.getId())
                        .build())
                .build();
        messageService.pushNotification(content);*/

        return toRequestCheckoutResponse(
                requestRepository.findById(saved.getId())
                        .orElseThrow(() -> newExceptionWithId(saved.getId())));
    }

    @Override
    public RequestCheckOutResponse checkinWithTechnicianId(Long requestId, Long userId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> newExceptionWithId(requestId));

        request.setTechnician(userRepository.findById(userId)
                .orElseThrow(() -> newExceptionWithId(userId)));
        request.setStatus("ARRIVED");
        request.setArriveTime(LocalDateTime.now());

        return toRequestCheckoutResponse(requestRepository.save(request));
    }

    @Override
    public RequestCheckOutResponse update(Long requestId, CheckinRequest request) {
        return toRequestCheckoutResponse(null);
    }

    @Override
    public RequestCheckOutResponse confirm(Long requestId) {
        return toRequestCheckoutResponse(null);
    }

    @Override
    public RequestCheckOutResponse finishRepairAndMaintenance(Long requestId) {
        Request result = requestRepository.findById(requestId)
                .orElseThrow(() -> newExceptionWithId(requestId));

        result.setStatus("WORK COMPLETED");

        Request saved = requestRepository.save(result);

        /*final String body = "Your car's repair/maintenance is completed." +
                "Please retrieve your car at " + saved.getProvider().getName() +
                ". Total cost is: " +
                "[Enter Price Here]" + " VND";
        Message content = Message.builder()
                .setToken(saved.getVehicle().getUser().getDeviceToken())
                .setNotification(Notification.builder()
                        .setTitle("The repair/maintenance is finished.")
                        .setBody(body)
                        .build())
                .build();
        messageService.pushNotification(content);*/

        return toRequestCheckoutResponse(saved);
    }

    @Override
    public RequestCheckOutResponse checkout(Long requestId) {
        Request result = requestRepository.findById(requestId)
                .orElseThrow();

        result.setStatus("FINISHED");
        result.setCheckoutTime(LocalDateTime.now());

        Request saved = requestRepository.save(result);

        /*final String body = "Your booking is completed. " +
                "Please give us your feedback and ratings about provider: " +
                saved.getProvider().getName();
        Message content = Message.builder()
                .setToken(saved.getVehicle().getUser().getDeviceToken())
                .setNotification(Notification.builder()
                        .setBody(body)
                        .setTitle("Your car booking is finished.")
                        .build())
                .build();
        messageService.pushNotification(content);*/

        return toRequestCheckoutResponse(saved);
    }

    @Override
    public RequestCheckOutResponse cancel(Long requestId) {
        Request result = requestRepository.findById(requestId)
                .orElseThrow();

        result.setStatus("CANCELED");

        return toRequestCheckoutResponse(requestRepository.save(result));
    }

    @Override
    public FeedbackResponse sendFeedback(Long requestId, FeedbackRequest feedbackRequest) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> newExceptionWithId(requestId));
        Feedback saved = feedbackRepository.save(Feedback.builder()
                .ratings(feedbackRequest.getRatings())
                .content(feedbackRequest.getContent())
                .imageUrls(feedbackRequest.getImageUrls())
                .user(request.getVehicle().getUser())
                .request(request)
                .build());
        return toFeedbackResponse(saved);
    }
}
