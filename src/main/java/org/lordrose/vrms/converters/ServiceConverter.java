package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.ServiceRequest;
import org.lordrose.vrms.models.responses.ServiceResponse;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceConverter {

    public static ServiceResponse toRequestServiceResponse(ServiceRequest service) {
        return ServiceResponse.builder()
                .build();
    }

    public static List<ServiceResponse> toRequestServiceResponses(Collection<ServiceRequest> services) {
        return services.stream()
                .map(ServiceConverter::toRequestServiceResponse)
                .collect(Collectors.toList());
    }
}
