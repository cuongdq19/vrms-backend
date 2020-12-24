package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.ServicePackage;
import org.lordrose.vrms.models.responses.ServicePackageResponse;

import static org.lordrose.vrms.converters.ServiceConverter.toServiceDetailResponses;

public class ServicePackageConverter {

    public static ServicePackageResponse toServicePackageResponse(ServicePackage servicePackage) {
        return ServicePackageResponse.builder()
                .id(servicePackage.getId())
                .name(servicePackage.getName())
                .milestone(servicePackage.getMilestone())
                .sectionId(servicePackage.getSection().getId())
                .sectionName(servicePackage.getSection().getName())
                .providerId(servicePackage.getProvider().getId())
                .packagedServices(toServiceDetailResponses(servicePackage.getPackagedServices()))
                .build();
    }
}
