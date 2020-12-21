package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.Service;
import org.lordrose.vrms.domains.ServiceRequest;
import org.lordrose.vrms.domains.ServiceTypeDetail;
import org.lordrose.vrms.domains.VehiclePart;
import org.lordrose.vrms.models.responses.ServiceDetailResponse;
import org.lordrose.vrms.models.responses.ServiceOptionResponse;
import org.lordrose.vrms.models.responses.ServicePriceDetailResponse;
import org.lordrose.vrms.models.responses.ServiceResponse;
import org.lordrose.vrms.models.responses.ServiceTypeDetailResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.lordrose.vrms.converters.ModelGroupConverter.toGroupResponse;
import static org.lordrose.vrms.converters.PartConverter.toEmptyModelPartResponses;

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

    public static List<ServiceDetailResponse> toServiceDetailResponses(List<Service> services) {
        return services.stream()
                .map(service -> ServiceDetailResponse.builder()
                        .id(service.getId())
                        .price(service.getPrice())
                        .group(toGroupResponse(service.getModelGroup()))
                        .build())
                .collect(Collectors.toList());
    }

    public static List<ServiceResponse> toServiceResponses(Collection<Service> services) {
        Map<ServiceTypeDetail, List<Service>> byGroup = new HashMap<>(services.stream()
                .collect(Collectors.groupingBy(Service::getTypeDetail)));
        List<ServiceResponse> responses = new ArrayList<>();
        byGroup.forEach(
                (typeDetail, serviceList) ->
                        responses.add(toServiceResponse(typeDetail, serviceList))
        );
        return responses;
    }

    public static List<ServiceResponse> toAllServicesResponses(Collection<Service> services,
                                                               List<ServiceTypeDetail> allTypes) {
        Map<ServiceTypeDetail, List<Service>> byType = new LinkedHashMap<>();
        allTypes.forEach(type -> byType.put(type, Collections.emptyList()));
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
                .serviceId(service.getId())
                .serviceName(service.getName())
                .price(service.getPrice())
                .build();
    }

    public static List<ServiceOptionResponse> toServiceOptionResponses(Collection<Service> services) {
        return services.stream()
                .map(ServiceConverter::toServiceOptionResponse)
                .collect(Collectors.toList());
    }
}
