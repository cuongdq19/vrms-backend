package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.Service;
import org.lordrose.vrms.domains.ServiceRequest;
import org.lordrose.vrms.domains.ServiceTypeDetail;
import org.lordrose.vrms.domains.VehicleModel;
import org.lordrose.vrms.domains.VehiclePart;
import org.lordrose.vrms.models.responses.ServiceCheckoutResponse;
import org.lordrose.vrms.models.responses.ServiceDetailResponse;
import org.lordrose.vrms.models.responses.ServiceHistoryResponse;
import org.lordrose.vrms.models.responses.ServiceOptionResponse;
import org.lordrose.vrms.models.responses.ServicePriceDetailResponse;
import org.lordrose.vrms.models.responses.ServiceResponse;
import org.lordrose.vrms.models.responses.ServiceTypeDetailResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.lordrose.vrms.converters.PartConverter.toEmptyModelPartResponses;
import static org.lordrose.vrms.converters.PartConverter.toEmptyModelServicePartResponses;
import static org.lordrose.vrms.converters.PartConverter.toPartCheckoutResponses;
import static org.lordrose.vrms.converters.PartConverter.toServicePartHistoryResponses;

public class ServiceConverter {

    public static ServicePriceDetailResponse toServicePriceDetailResponse(Service service,
                                                                    Collection<VehiclePart> parts) {
        return ServicePriceDetailResponse.builder()
                .serviceId(service.getId())
                .serviceName(service.getName())
                .servicePrice(service.getPrice())
                .typeDetail(toServiceTypeDetailResponse(service.getTypeDetail()))
                .parts(toEmptyModelPartResponses(parts))
                .build();
    }

    public static ServiceTypeDetailResponse toServiceTypeDetailResponse(ServiceTypeDetail typeDetail) {
        return ServiceTypeDetailResponse.builder()
                .id(typeDetail.getId())
                .typeId(typeDetail.getType().getId())
                .typeName(typeDetail.getType().getName())
                .sectionId(typeDetail.getSection().getId())
                .sectionName(typeDetail.getSection().getName())
                .sectionImageUrl(typeDetail.getSection().getImageUrl())
                .build();
    }

    public static List<ServiceTypeDetailResponse> toServiceTypeDetailResponses(Collection<ServiceTypeDetail> typeDetails) {
        return typeDetails.stream()
                .map(ServiceConverter::toServiceTypeDetailResponse)
                .collect(Collectors.toList());
    }

    public static ServiceResponse toServiceResponse(Service service) {
        return ServiceResponse.builder()
                .typeDetail(toServiceTypeDetailResponse(service.getTypeDetail()))
                .serviceDetails(toServiceDetailResponses(List.of(service)))
                .build();
    }

    public static ServiceResponse toServiceResponse(ServiceTypeDetail detail, List<Service> services) {
        return ServiceResponse.builder()
                .typeDetail(toServiceTypeDetailResponse(detail))
                .serviceDetails(toServiceDetailResponses(services))
                .build();
    }

    public static List<ServiceDetailResponse> toServiceDetailResponses(Collection<Service> services) {
        return services.stream()
                .map(service -> ServiceDetailResponse.builder()
                        .id(service.getId())
                        .name(service.getName())
                        .price(service.getPrice())
                        .parts(toEmptyModelServicePartResponses(service.getPartSet()))
                        .modelIds(service.getModels() == null ? Collections.emptyList() :
                                service.getModels().stream()
                                        .map(VehicleModel::getId)
                                        .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    public static List<ServiceResponse> toAllServicesResponses(Collection<Service> services) {
        Map<ServiceTypeDetail, List<Service>> byType = services.stream()
                .collect(Collectors.groupingBy(Service::getTypeDetail));
        List<ServiceResponse> responses = new ArrayList<>();
        byType.forEach(
                (typeDetail, serviceList) ->
                        responses.add(toServiceResponse(typeDetail, serviceList))
        );
        return responses;
    }

    public static List<ServiceResponse> toAllServicesResponses(Collection<ServiceTypeDetail> typeDetails,
                                                               Collection<Service> services) {
        Map<ServiceTypeDetail, List<Service>> byType = new LinkedHashMap<>();
        typeDetails.forEach(typeDetail -> byType.put(typeDetail, Collections.emptyList()));
        byType.putAll(services.stream()
                .collect(Collectors.groupingBy(Service::getTypeDetail)));
        List<ServiceResponse> responses = new ArrayList<>();
        byType.forEach(
                (typeDetail, serviceList) ->
                        responses.add(toServiceResponse(typeDetail, serviceList))
        );
        return responses;
    }

    public static Object toServiceRequestResponse(ServiceRequest service) {
        return null;
    }

    public static List<Object> toRequestServiceResponses(Collection<ServiceRequest> services) {
        return services.stream()
                .map(ServiceConverter::toServiceRequestResponse)
                .collect(Collectors.toList());
    }

    public static ServiceOptionResponse toServiceOptionResponse(Service service) {
        return ServiceOptionResponse.builder()
                .id(service.getId())
                .name(service.getName())
                .price(service.getPrice())
                .parts(toEmptyModelServicePartResponses(service.getPartSet()))
                .total(service.getPrice() + service.getPartSet().stream()
                        .mapToDouble(servicePart -> servicePart.getQuantity() *
                                servicePart.getPart().getPrice())
                        .sum())
                .build();
    }

    public static List<ServiceOptionResponse> toServiceOptionResponses(Collection<Service> services) {
        return services.stream()
                .map(ServiceConverter::toServiceOptionResponse)
                .collect(Collectors.toList());
    }

    public static ServiceHistoryResponse toServiceHistoryResponse(ServiceRequest service) {
        return ServiceHistoryResponse.builder()
                .id(service.returnServiceId())
                .price(service.getPrice())
                .serviceName(service.getServiceName())
                .typeDetailId(service.returnTypeDetailId())
                .typeId(service.returnTypeId())
                .typeName(service.returnTypeName())
                .sectionId(service.returnSectionId())
                .sectionName(service.returnSectionName())
                .parts(toServicePartHistoryResponses(service.getRequestParts()))
                .build();
    }

    public static List<ServiceHistoryResponse> toServiceHistoryResponses(Collection<ServiceRequest> services) {
        return services.stream()
                .map(ServiceConverter::toServiceHistoryResponse)
                .collect(Collectors.toList());
    }

    public static ServiceCheckoutResponse toServiceCheckoutResponse(ServiceRequest service) {
        return ServiceCheckoutResponse.builder()
                .id(service.getId())
                .serviceId(service.returnServiceId())
                .serviceName(service.getServiceName())
                .servicePrice(service.getPrice())
                .note(service.getNote())
                .parts(toPartCheckoutResponses(service.getRequestParts()))
                .isIncurred(service.getIsIncurred())
                .build();
    }

    public static List<ServiceCheckoutResponse> toServiceCheckoutResponses(Collection<ServiceRequest> services) {
        return services.stream()
                .map(ServiceConverter::toServiceCheckoutResponse)
                .collect(Collectors.toList());
    }
}
