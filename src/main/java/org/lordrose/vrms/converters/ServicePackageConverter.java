package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.MaintenancePackage;
import org.lordrose.vrms.domains.ServiceRequest;
import org.lordrose.vrms.models.responses.PackageCheckoutResponse;
import org.lordrose.vrms.models.responses.PackageHistoryResponse;
import org.lordrose.vrms.models.responses.ServicePackageResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.lordrose.vrms.converters.ServiceConverter.toServiceCheckoutResponses;
import static org.lordrose.vrms.converters.ServiceConverter.toServiceDetailResponses;
import static org.lordrose.vrms.converters.ServiceConverter.toServiceHistoryResponses;

public class ServicePackageConverter {

    public static ServicePackageResponse toServicePackageResponse(MaintenancePackage maintenancePackage) {
        return ServicePackageResponse.builder()
                .id(maintenancePackage.getId())
                .name(maintenancePackage.getName())
                .milestone(maintenancePackage.getMilestone())
                .sectionId(maintenancePackage.returnSectionId())
                .sectionName(maintenancePackage.returnSectionName())
                .packagedServices(toServiceDetailResponses(maintenancePackage.getPackagedServices()))
                .build();
    }

    public static List<ServicePackageResponse> toServicePackageResponses(Collection<MaintenancePackage> maintenancePackages) {
        return maintenancePackages.stream()
                .map(ServicePackageConverter::toServicePackageResponse)
                .collect(Collectors.toList());
    }

    public static List<PackageHistoryResponse> toPackageHistoryResponses(Collection<ServiceRequest> services) {
        Map<MaintenancePackage, List<ServiceRequest>> resultMap = services.stream()
                .filter(service -> service.getMaintenancePackage() != null)
                .collect(Collectors.groupingBy(ServiceRequest::getMaintenancePackage));
        List<PackageHistoryResponse> responses = new ArrayList<>();

        resultMap.forEach((maintenancePackage, serviceRequests) ->
                responses.add(PackageHistoryResponse.builder()
                        .packageId(maintenancePackage.getId())
                        .packageName(maintenancePackage.getName())
                        .milestone(maintenancePackage.getMilestone())
                        .sectionId(maintenancePackage.returnSectionId())
                        .sectionName(maintenancePackage.returnSectionName())
                        .services(toServiceHistoryResponses(serviceRequests))
                        .build()));

        return responses;
    }

    public static List<PackageCheckoutResponse> toPackageCheckoutResponses(Collection<ServiceRequest> services) {
        Map<MaintenancePackage, List<ServiceRequest>> resultMap = services.stream()
                .filter(service -> service.getMaintenancePackage() != null)
                .collect(Collectors.groupingBy(ServiceRequest::getMaintenancePackage));
        List<PackageCheckoutResponse> responses = new ArrayList<>();

        resultMap.forEach((maintenancePackage, serviceRequests) ->
                responses.add(PackageCheckoutResponse.builder()
                        .packageId(maintenancePackage.getId())
                        .packageName(maintenancePackage.getName())
                        .milestone(maintenancePackage.getMilestone())
                        .sectionId(maintenancePackage.returnSectionId())
                        .sectionName(maintenancePackage.returnSectionName())
                        .services(toServiceCheckoutResponses(serviceRequests))
                        .build()));

        return responses;
    }
}
