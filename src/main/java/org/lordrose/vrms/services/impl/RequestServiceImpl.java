package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.Feedback;
import org.lordrose.vrms.domains.MaintenancePackage;
import org.lordrose.vrms.domains.Provider;
import org.lordrose.vrms.domains.Request;
import org.lordrose.vrms.domains.Service;
import org.lordrose.vrms.domains.ServiceRequest;
import org.lordrose.vrms.domains.ServiceRequestPart;
import org.lordrose.vrms.domains.Vehicle;
import org.lordrose.vrms.domains.VehiclePart;
import org.lordrose.vrms.domains.constants.RequestStatus;
import org.lordrose.vrms.exceptions.InvalidArgumentException;
import org.lordrose.vrms.models.requests.ExpenseRequest;
import org.lordrose.vrms.models.requests.FeedbackRequest;
import org.lordrose.vrms.models.requests.RequestIncurredUpdateRequest;
import org.lordrose.vrms.models.requests.RequestInfoRequest;
import org.lordrose.vrms.models.responses.FeedbackResponse;
import org.lordrose.vrms.models.responses.RequestCheckOutResponse;
import org.lordrose.vrms.models.responses.RequestHistoryDetailResponse;
import org.lordrose.vrms.repositories.FeedbackRepository;
import org.lordrose.vrms.repositories.MaintenancePackageRepository;
import org.lordrose.vrms.repositories.ProviderRepository;
import org.lordrose.vrms.repositories.RequestRepository;
import org.lordrose.vrms.repositories.ServiceRepository;
import org.lordrose.vrms.repositories.ServiceRequestPartRepository;
import org.lordrose.vrms.repositories.ServiceRequestRepository;
import org.lordrose.vrms.repositories.UserRepository;
import org.lordrose.vrms.repositories.VehiclePartRepository;
import org.lordrose.vrms.repositories.VehicleRepository;
import org.lordrose.vrms.services.RequestService;
import org.lordrose.vrms.services.StorageService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
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
import static org.lordrose.vrms.utils.RequestValidator.validatePartsRequest;

@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class RequestServiceImpl implements RequestService {
    
    private final RequestRepository requestRepository;
    private final ProviderRepository providerRepository;
    private final VehicleRepository vehicleRepository;
    private final ServiceRepository serviceRepository;
    private final ServiceRequestRepository serviceRequestRepository;
    private final VehiclePartRepository partRepository;
    private final ServiceRequestPartRepository requestPartRepository;
    private final UserRepository userRepository;
    private final FeedbackRepository feedbackRepository;
    private final MaintenancePackageRepository packageRepository;

    private final StorageService storageService;
    private final ReminderServiceImpl reminderService;
    
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

    @Transactional
    @Override
    public RequestCheckOutResponse findRequestById(Long requestId) {
        Request result = requestRepository.findById(requestId)
                .orElseThrow(() -> newExceptionWithId(requestId));
        return toRequestCheckoutResponse(result);
    }

    @Transactional
    @Override
    public Object create(RequestInfoRequest request) {
        List<Long> packageIds = request.getPackageIds();
        List<Long> serviceIds = request.getServiceIds();

        if (packageIds.isEmpty() && serviceIds.isEmpty()) {
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
                .status(RequestStatus.ACCEPTED)
                .vehicle(vehicle)
                .provider(provider)
                .build());
        boolean isIncurred = isIncurred(saved);

        List<ServiceRequest> services = new ArrayList<>();
        serviceIds.forEach(serviceId -> {
            Service service = serviceRepository.findById(serviceId)
                    .orElseThrow(() -> newExceptionWithId(serviceId));
            List<ServiceRequestPart> list = new ArrayList<>();

            service.getPartSet().forEach(servicePart -> list.add(ServiceRequestPart.builder()
                    .quantity(servicePart.getQuantity())
                    .price(servicePart.getPart().getPrice())
                    .vehiclePart(servicePart.getPart())
                    .build()));

            ServiceRequest serviceRequest = serviceRequestRepository.save(ServiceRequest.builder()
                    .serviceName(service.getName())
                    .price(service.getPrice())
                    .note("")
                    .isIncurred(isIncurred)
                    .isActive(true)
                    .service(service)
                    .request(saved)
                    .build());

            list.forEach(item -> item.setServiceRequest(serviceRequest));

            serviceRequest.setRequestParts(new LinkedHashSet<>(requestPartRepository.saveAll(list)));

            services.add(serviceRequest);
        });

        packageIds.forEach(packageId -> {
            MaintenancePackage maintenancePackage = packageRepository.findById(packageId)
                    .orElseThrow(() -> newExceptionWithId(packageId));
            List<ServiceRequestPart> list = new ArrayList<>();

            maintenancePackage.getPackagedServices().forEach(service -> {
                service.getPartSet().forEach(servicePart -> list.add(ServiceRequestPart.builder()
                        .quantity(servicePart.getQuantity())
                        .price(servicePart.getPart().getPrice())
                        .vehiclePart(servicePart.getPart())
                        .build()));

                ServiceRequest serviceRequest = serviceRequestRepository.save(ServiceRequest.builder()
                        .serviceName(service.getName())
                        .price(service.getPrice())
                        .note("")
                        .isIncurred(isIncurred)
                        .isActive(true)
                        .service(service)
                        .maintenancePackage(maintenancePackage)
                        .request(saved)
                        .build());

                list.forEach(item -> item.setServiceRequest(serviceRequest));
                serviceRequest.setRequestParts(new LinkedHashSet<>(requestPartRepository.saveAll(list)));
                services.add(serviceRequest);
            });
        });

        saved.setServices(new LinkedHashSet<>(services));

        return toRequestCheckoutResponse(
                requestRepository.findById(saved.getId())
                        .orElseThrow(() -> newExceptionWithId(saved.getId())));
    }

    @Transactional
    @Override
    public RequestCheckOutResponse checkinWithTechnicianId(Long requestId, Long userId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> newExceptionWithId(requestId));

        request.setTechnician(userRepository.findById(userId)
                .orElseThrow(() -> newExceptionWithId(userId)));
        request.setStatus(RequestStatus.ARRIVED);
        request.setArriveTime(LocalDateTime.now());

        return toRequestCheckoutResponse(requestRepository.save(request));
    }

    @Transactional
    @Override
    public RequestCheckOutResponse update(Long requestId, RequestIncurredUpdateRequest request) {
        Set<Long> disables = request.getDisables();
        Set<Long> enables = request.getEnables();
        Map<Long, Map<Long, Map<Long, Double>>> packageMap = request.getPackageMap();
        Map<Long, Map<Long, Double>> servicePartMap = request.getServicePartMap();
        Set<ExpenseRequest> expenses = request.getExpenses();
        Request result = requestRepository.findById(requestId)
                .orElseThrow(() -> newExceptionWithId(requestId));

        boolean isIncurred = isIncurred(result);
        if (!isIncurred) {
            serviceRequestRepository.deleteAll(result.getServices());
            result.getServices().clear();
        }

        List<ServiceRequest> services = new ArrayList<>();
        servicePartMap.forEach((serviceId, values) -> {
            Service service = serviceRepository.findById(serviceId)
                    .orElseThrow(() -> newExceptionWithId(serviceId));
            List<ServiceRequestPart> list = new ArrayList<>();
            ServiceRequest serviceRequest = serviceRequestRepository.save(ServiceRequest.builder()
                    .serviceName(service.getName())
                    .price(service.getPrice())
                    .note("")
                    .isIncurred(isIncurred)
                    .isActive(true)
                    .service(service)
                    .request(result)
                    .build());

            List<VehiclePart> parts = partRepository.findAllById(values.keySet());

            validatePartsRequest(parts, values);

            parts.forEach(part -> list.add(ServiceRequestPart.builder()
                    .quantity(values.get(part.getId()))
                    .price(part.getPrice())
                    .vehiclePart(part)
                    .serviceRequest(serviceRequest)
                    .build()));
            serviceRequest.setRequestParts(
                    new LinkedHashSet<>(requestPartRepository.saveAll(list)));
            services.add(serviceRequest);
        });

        expenses.forEach(expense -> {
            MaintenancePackage maintenancePackage = null;
            if (expense.getMaintenancePackageId() != null)
                maintenancePackage = packageRepository.findById(expense.getMaintenancePackageId())
                        .orElseThrow(() -> newExceptionWithId(expense.getMaintenancePackageId()));
            List<ServiceRequestPart> list = new ArrayList<>();
            ServiceRequest serviceRequest = serviceRequestRepository.save(ServiceRequest.builder()
                    .serviceName(expense.getName())
                    .price(expense.getPrice())
                    .note(expense.getNote())
                    .isIncurred(isIncurred)
                    .isActive(true)
                    .service(null)
                    .maintenancePackage(maintenancePackage)
                    .request(result)
                    .build());

            List<VehiclePart> parts = partRepository.findAllById(expense.getParts().keySet());

            validatePartsRequest(parts, expense.getParts());

            parts.forEach(part -> list.add(ServiceRequestPart.builder()
                    .quantity(expense.getParts().get(part.getId()))
                    .price(part.getPrice())
                    .vehiclePart(part)
                    .serviceRequest(serviceRequest)
                    .build()));
            serviceRequest.setRequestParts(
                    new LinkedHashSet<>(requestPartRepository.saveAll(list)));
            services.add(serviceRequest);
        });

        packageMap.forEach((packageId, serviceMap) -> {
            MaintenancePackage maintenancePackage = packageRepository.findById(packageId)
                    .orElseThrow(() -> newExceptionWithId(packageId));

            serviceMap.forEach((serviceId, partMap) -> {
                Service service = serviceRepository.findById(serviceId)
                        .orElseThrow(() -> newExceptionWithId(serviceId));
                List<ServiceRequestPart> list = new ArrayList<>();
                ServiceRequest serviceRequest = serviceRequestRepository.save(ServiceRequest.builder()
                        .serviceName(service.getName())
                        .price(service.getPrice())
                        .note("")
                        .isIncurred(isIncurred)
                        .isActive(true)
                        .service(service)
                        .maintenancePackage(maintenancePackage)
                        .request(result)
                        .build());

                List<VehiclePart> parts = partRepository.findAllById(partMap.keySet());

                validatePartsRequest(parts, partMap);

                parts.forEach(part -> list.add(ServiceRequestPart.builder()
                        .quantity(partMap.get(part.getId()))
                        .price(part.getPrice())
                        .vehiclePart(part)
                        .serviceRequest(serviceRequest)
                        .build()));
                serviceRequest.setRequestParts(
                        new LinkedHashSet<>(requestPartRepository.saveAll(list)));
                services.add(serviceRequest);
            });
        });

        result.getServices().addAll(serviceRequestRepository.saveAll(services));

        serviceRequestRepository.saveAll(
                serviceRequestRepository.findAllByRequestIdAndIdIn(result.getId(), disables).stream()
                        .peek(serviceRequest -> serviceRequest.setIsActive(false))
                        .collect(Collectors.toList()));

        serviceRequestRepository.saveAll(
                serviceRequestRepository.findAllByRequestIdAndIdIn(result.getId(), enables).stream()
                        .peek(serviceRequest -> serviceRequest.setIsActive(true))
                        .collect(Collectors.toList()));

        return toRequestCheckoutResponse(
                requestRepository.findById(result.getId())
                        .orElseThrow());
    }

    @Transactional
    @Override
    public RequestCheckOutResponse confirm(Long requestId) {
        Request result = requestRepository.findById(requestId)
                .orElseThrow(() -> newExceptionWithId(requestId));

        result.setStatus(RequestStatus.CONFIRMED);

        return toRequestCheckoutResponse(requestRepository.save(result));
    }

    @Transactional
    @Override
    public RequestCheckOutResponse finishRepairAndMaintenance(Long requestId) {
        Request result = requestRepository.findById(requestId)
                .orElseThrow(() -> newExceptionWithId(requestId));

        result.setStatus(RequestStatus.WORK_COMPLETED);

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

        return toRequestCheckoutResponse(requestRepository.save(result));
    }

    @Transactional
    @Override
    public RequestCheckOutResponse checkout(Long requestId) {
        Request result = requestRepository.findById(requestId)
                .orElseThrow(() -> newExceptionWithId(requestId));

        result.setStatus(RequestStatus.FINISHED);
        result.setCheckoutTime(LocalDateTime.now());

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

        reminderService.createReminders(result.getServices().stream()
                .flatMap(serviceRequest -> serviceRequest.getRequestParts().stream())
                .collect(Collectors.toList()));

        return toRequestCheckoutResponse(requestRepository.save(result));
    }

    @Override
    public RequestCheckOutResponse cancel(Long requestId) {
        Request result = requestRepository.findById(requestId)
                .orElseThrow();

        result.setStatus(RequestStatus.CANCELED);

        return toRequestCheckoutResponse(requestRepository.save(result));
    }

    @Override
    public FeedbackResponse sendFeedback(Long requestId, FeedbackRequest feedbackRequest,
                                         MultipartFile[] images) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> newExceptionWithId(requestId));
        if (feedbackRequest.getRatings() == null ||
                feedbackRequest.getRatings() < 1 ||
                feedbackRequest.getRatings() > 5) {
            throw new InvalidArgumentException("Invalid value for request ratings.");
        }

        Feedback saved = feedbackRepository.save(Feedback.builder()
                .ratings(feedbackRequest.getRatings())
                .content(feedbackRequest.getContent())
                .imageUrls(storageService.uploadFiles(images))
                .request(request)
                .build());
        return toFeedbackResponse(saved);
    }

    private boolean isIncurred(Request request) {
        boolean isIncurred;
        switch (request.getStatus()) {
            case ACCEPTED:
            case ARRIVED:
            case CANCELED:
                isIncurred = false;
                break;
            case CONFIRMED:
            case WORK_COMPLETED:
            case FINISHED:
                isIncurred = true;
                break;
            default:
                throw new InvalidArgumentException("Invalid status of request");
        }
        return isIncurred;
    }
}
