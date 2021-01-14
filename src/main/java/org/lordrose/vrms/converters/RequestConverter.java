package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.Request;
import org.lordrose.vrms.models.responses.RequestCheckOutResponse;
import org.lordrose.vrms.models.responses.RequestHistoryDetailResponse;
import org.lordrose.vrms.models.responses.RequestHistoryResponse;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.lordrose.vrms.converters.FeedbackConverter.toFeedbackHistoryResponse;
import static org.lordrose.vrms.converters.MaintenancePackageConverter.toPackageCheckoutResponses;
import static org.lordrose.vrms.converters.ProviderConverter.toProviderHistoryResponse;
import static org.lordrose.vrms.converters.ProviderConverter.toProviderResponse;
import static org.lordrose.vrms.converters.ServiceConverter.toRequestServiceResponses;
import static org.lordrose.vrms.converters.ServiceConverter.toServiceCheckoutResponses;
import static org.lordrose.vrms.converters.UserConverter.toRequestUserInfoResponse;
import static org.lordrose.vrms.converters.UserConverter.toTechnicianHistoryResponse;
import static org.lordrose.vrms.converters.UserConverter.toUserVehicleHistoryResponse;
import static org.lordrose.vrms.converters.VehicleModelConverter.toModelResponse;
import static org.lordrose.vrms.utils.DateTimeUtils.toSeconds;
import static org.lordrose.vrms.utils.FileUrlUtils.getUrlsAsArray;

public class RequestConverter {

    public static RequestHistoryResponse toRequestHistoryResponse(Request request) {
        if (request == null)
            return null;
        return RequestHistoryResponse.builder()
                .id(request.getId())
                .bookingTime(toSeconds(request.getBookingTime()))
                .note(request.getNote())
                .status(request.getStatus().textValue)
                .technicianName(request.getTechnician().getFullName())
                .model(toModelResponse(request.getVehicle().getModel()))
                .services(toRequestServiceResponses(request.getServices()))
                .provider(toProviderResponse(request.getProvider()))
                .build();
    }

    public static RequestHistoryDetailResponse toRequestHistoryDetailResponse(Request request) {
        return RequestHistoryDetailResponse.builder()
                .id(request.getId())
                .bookingTime(toSeconds(request.getBookingTime()))
                .arriveTime(toSeconds(request.getArriveTime()))
                .checkoutTime(toSeconds(request.getCheckoutTime()))
                .note(request.getNote())
                .status(request.getStatus().textValue)
                .packages(toPackageCheckoutResponses(
                        request.getServices().stream()
                                .filter(service -> service.getMaintenancePackage() != null)
                                .collect(Collectors.toList())))
                .services(toServiceCheckoutResponses(
                        request.getServices().stream()
                                .filter(service -> service.getMaintenancePackage() == null)
                                .collect(Collectors.toList())))
                .technician(toTechnicianHistoryResponse(request.getTechnician()))
                .userVehicle(toUserVehicleHistoryResponse(request.getVehicle()))
                .provider(toProviderHistoryResponse(request.getProvider()))
                .feedback(toFeedbackHistoryResponse(request.getFeedback()))
                .build();
    }

    public static List<RequestHistoryDetailResponse> toRequestHistoryDetailResponses(Collection<Request> requests) {
        return requests.stream()
                .sorted(Comparator.comparing(Request::getCreateAt).reversed())
                .map(RequestConverter::toRequestHistoryDetailResponse)
                .collect(Collectors.toList());
    }

    public static RequestCheckOutResponse toRequestCheckoutResponse(Request request) {
        return RequestCheckOutResponse.builder()
                .id(request.getId())
                .bookingTime(toSeconds(request.getBookingTime()))
                .note(request.getNote())
                .status(request.getStatus().textValue)
                .imageUrls(getUrlsAsArray(request.getImageUrls()))
                .user(toRequestUserInfoResponse(request.getVehicle()))
                .packages(toPackageCheckoutResponses(
                        request.getServices().stream()
                                .filter(service -> service.getMaintenancePackage() != null)
                                .collect(Collectors.toList())))
                .services(toServiceCheckoutResponses(
                        request.getServices().stream()
                                .filter(service -> service.getMaintenancePackage() == null)
                                .collect(Collectors.toList())))
                .build();
    }

    public static List<RequestCheckOutResponse> toRequestCheckoutResponses(Collection<Request> requests) {
        return requests.stream()
                .map(RequestConverter::toRequestCheckoutResponse)
                .collect(Collectors.toList());
    }
}
