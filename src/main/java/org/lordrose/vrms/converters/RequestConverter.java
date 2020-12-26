package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.Request;
import org.lordrose.vrms.models.responses.RequestCheckOutResponse;
import org.lordrose.vrms.models.responses.RequestHistoryDetailResponse;
import org.lordrose.vrms.models.responses.RequestHistoryResponse;
import org.lordrose.vrms.models.responses.ServiceCheckoutResponse;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.lordrose.vrms.converters.ExpenseConverter.toExpenseResponses;
import static org.lordrose.vrms.converters.PartConverter.toPartCheckoutResponses;
import static org.lordrose.vrms.converters.PartConverter.toPartDetailResponses;
import static org.lordrose.vrms.converters.ProviderConverter.toProviderResponse;
import static org.lordrose.vrms.converters.ServiceConverter.toRequestServiceResponses;
import static org.lordrose.vrms.converters.UserConverter.toRequestUserInfoResponse;
import static org.lordrose.vrms.converters.VehicleModelConverter.toModelResponse;
import static org.lordrose.vrms.utils.DateTimeUtils.toSeconds;
import static org.lordrose.vrms.utils.FileUrlUtils.getUrlsAsArray;

public class RequestConverter {

    public static RequestHistoryResponse toRequestHistoryResponse(Request request) {
        if (request == null)
            return null;
        if (request.getExpenses() == null)
            request.setExpenses(Collections.emptySet());
        return RequestHistoryResponse.builder()
                .id(request.getId())
                .bookingTime(toSeconds(request.getBookingTime()))
                .note(request.getNote())
                .status(request.getStatus())
                .technicianName(request.getTechnician().getFullName())
                .model(toModelResponse(request.getVehicle().getModel()))
                .services(toRequestServiceResponses(request.getServices()))
                .parts(toPartDetailResponses(request.getParts()))
                .expenses(toExpenseResponses(request.getExpenses()))
                .provider(toProviderResponse(request.getProvider()))
                .build();
    }

    public static RequestHistoryDetailResponse toRequestHistoryDetailResponse(Request request) {
        return RequestHistoryDetailResponse.builder()
                .build();
    }

    public static List<RequestHistoryDetailResponse> toRequestHistoryDetailResponses(Collection<Request> requests) {
        return requests.stream()
                .map(RequestConverter::toRequestHistoryDetailResponse)
                .collect(Collectors.toList());
    }

    public static RequestCheckOutResponse toRequestCheckoutResponse(Request request) {
        return RequestCheckOutResponse.builder()
                .id(request.getId())
                .bookingTime(toSeconds(request.getBookingTime()))
                .note(request.getNote())
                .status(request.getStatus())
                .imageUrls(getUrlsAsArray(request.getImageUrls()))
                .user(toRequestUserInfoResponse(request.getVehicle().getUser()))
                .packages(null)
                .services(request.getServices().stream()
                        .map(service -> ServiceCheckoutResponse.builder()
                                .id(service.getId())
                                .serviceId(service.getService().getId())
                                .serviceName(service.getService().getName())
                                .servicePrice(service.getPrice())
                                .parts(toPartCheckoutResponses(service.getRequestParts()))
                                .build())
                        .collect(Collectors.toList()))
                .parts(toPartDetailResponses(request.getParts()))
                .expenses(toExpenseResponses(request.getExpenses()))
                .build();
    }

    public static List<RequestCheckOutResponse> toRequestCheckoutResponses(Collection<Request> requests) {
        return requests.stream()
                .map(RequestConverter::toRequestCheckoutResponse)
                .collect(Collectors.toList());
    }
}
