package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.Feedback;
import org.lordrose.vrms.domains.IncurredExpense;
import org.lordrose.vrms.domains.IncurredPart;
import org.lordrose.vrms.domains.Provider;
import org.lordrose.vrms.domains.Request;
import org.lordrose.vrms.domains.Service;
import org.lordrose.vrms.domains.ServiceRequest;
import org.lordrose.vrms.domains.ServiceRequestPart;
import org.lordrose.vrms.domains.Vehicle;
import org.lordrose.vrms.exceptions.InvalidArgumentException;
import org.lordrose.vrms.models.requests.CheckinRequest;
import org.lordrose.vrms.models.requests.ExpenseRequest;
import org.lordrose.vrms.models.requests.FeedbackRequest;
import org.lordrose.vrms.models.requests.RequestInfoRequest;
import org.lordrose.vrms.models.responses.FeedbackResponse;
import org.lordrose.vrms.models.responses.RequestCheckOutResponse;
import org.lordrose.vrms.models.responses.RequestHistoryDetailResponse;
import org.lordrose.vrms.repositories.FeedbackRepository;
import org.lordrose.vrms.repositories.IncurredExpenseRepository;
import org.lordrose.vrms.repositories.IncurredPartRepository;
import org.lordrose.vrms.repositories.ProviderRepository;
import org.lordrose.vrms.repositories.RequestRepository;
import org.lordrose.vrms.repositories.ServiceRepository;
import org.lordrose.vrms.repositories.ServiceRequestPartRepository;
import org.lordrose.vrms.repositories.ServiceRequestRepository;
import org.lordrose.vrms.repositories.UserRepository;
import org.lordrose.vrms.repositories.VehiclePartRepository;
import org.lordrose.vrms.repositories.VehicleRepository;
import org.lordrose.vrms.services.RequestService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
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
    private final VehiclePartRepository partRepository;
    private final IncurredPartRepository incurredPartRepository;
    private final ServiceRequestPartRepository requestPartRepository;
    private final UserRepository userRepository;
    private final FeedbackRepository feedbackRepository;
    private final IncurredExpenseRepository expenseRepository;

    private final AccessoryServiceImpl accessoryService;
    
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

    @Override
    public Object create(RequestInfoRequest request) {
        Set<Long> packageIds = request.getPackageIds();
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
                .status("ACCEPTED")
                .vehicle(vehicle)
                .provider(provider)
                .build());

        List<ServiceRequest> services = new ArrayList<>();
        serviceIds.forEach(serviceId -> {
            Service service = serviceRepository.findById(serviceId)
                    .orElseThrow(() -> newExceptionWithId(serviceId));
            Set<ServiceRequestPart> list = new LinkedHashSet<>();

            service.getPartSet().forEach(servicePart -> {
                list.add(ServiceRequestPart.builder()
                        .quantity(servicePart.getQuantity())
                        .price(servicePart.getPart().getPrice())
                        .vehiclePart(servicePart.getPart())
                        .build());
            });

            ServiceRequest serviceRequest = serviceRequestRepository.save(ServiceRequest.builder()
                    .price(service.getPrice())
                    .service(service)
                    .request(saved)
                    .build());

            list.forEach(item -> item.setServiceRequest(serviceRequest));

            serviceRequest.setRequestParts(new LinkedHashSet<>(requestPartRepository.saveAll(list)));

            services.add(serviceRequest);
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
        request.setStatus("ARRIVED");
        request.setArriveTime(LocalDateTime.now());

        return toRequestCheckoutResponse(requestRepository.save(request));
    }

    @Override
    public RequestCheckOutResponse update(Long requestId, CheckinRequest request) {
        Set<Long> packageIds = request.getPackageIds();
        Set<ExpenseRequest> expenseRequests = request.getExpenses();
        List<Long> serviceIds = request.getServiceIds();
        if (packageIds.isEmpty() && serviceIds.isEmpty()) {
            throw new InvalidArgumentException("At least one field is required!");
        }

        Request result = requestRepository.findById(requestId)
                .orElseThrow(() -> newExceptionWithId(requestId));

        result.getServices().forEach(serviceRequestRepository::delete);
        List<ServiceRequest> services = new ArrayList<>();
        serviceIds.forEach(serviceId -> {
            Service service = serviceRepository.findById(serviceId)
                    .orElseThrow(() -> newExceptionWithId(serviceId));
            List<ServiceRequestPart> list = new ArrayList<>();
            ServiceRequest serviceRequest = serviceRequestRepository.save(ServiceRequest.builder()
                    .price(service.getPrice())
                    .service(service)
                    .request(result)
                    .build());
            service.getPartSet().forEach(servicePart -> {
                list.add(ServiceRequestPart.builder()
                        .quantity(servicePart.getQuantity())
                        .price(servicePart.getPart().getPrice())
                        .vehiclePart(servicePart.getPart())
                        .serviceRequest(serviceRequest)
                        .build());
            });
            serviceRequest.setRequestParts(
                    new LinkedHashSet<>(requestPartRepository.saveAll(list)));
            services.add(serviceRequest);
        });
        result.setServices(new LinkedHashSet<>(serviceRequestRepository.saveAll(services)));

        result.getExpenses().forEach(expenseRepository::delete);
        List<IncurredExpense> expenses = expenseRequests.stream()
                .map(expenseRequest -> {
                    IncurredExpense expense = expenseRepository.save(IncurredExpense.builder()
                            .name(expenseRequest.getName())
                            .price(expenseRequest.getPrice())
                            .description(expenseRequest.getDescription())
                            .request(result)
                            .build());

                    Set<IncurredPart> incurredParts =
                            partRepository.findAllById(expenseRequest.getParts().keySet()).stream()
                                    .map(part -> IncurredPart.builder()
                                            .quantity(expenseRequest.getParts().get(part.getId()))
                                        .price(part.getPrice())
                                        .vehiclePart(part)
                                        .expense(expense)
                                        .build())
                                    .collect(Collectors.toSet());

                    expense.setParts(new LinkedHashSet<>(incurredPartRepository.saveAll(incurredParts)));

                    return expense;
                })
                .collect(Collectors.toList());
        result.setExpenses(new LinkedHashSet<>(expenseRepository.saveAll(expenses)));

        return toRequestCheckoutResponse(
                requestRepository.findById(result.getId())
                        .orElseThrow(() -> newExceptionWithId(result.getId())));

    }

    @Transactional
    @Override
    public RequestCheckOutResponse confirm(Long requestId) {
        Request result = requestRepository.findById(requestId)
                .orElseThrow(() -> newExceptionWithId(requestId));

        result.setStatus("CONFIRMED");

        return toRequestCheckoutResponse(requestRepository.save(result));
    }

    @Transactional
    @Override
    public RequestCheckOutResponse finishRepairAndMaintenance(Long requestId) {
        Request result = requestRepository.findById(requestId)
                .orElseThrow(() -> newExceptionWithId(requestId));

        result.setStatus("WORK COMPLETED");

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
                .orElseThrow();

        result.setStatus("FINISHED");
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

        int count_1 = accessoryService.registerAccessoryFromPartRequests(
                result.getVehicle().getUser(), Collections.emptyList());

        int count_2 = accessoryService.registerAccessoryFromServiceRequestParts(
                result.getVehicle().getUser(), result.getServices().stream()
                        .flatMap(serviceRequest -> serviceRequest.getRequestParts().stream())
                        .collect(Collectors.toSet()));

        return toRequestCheckoutResponse(requestRepository.save(result));
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
