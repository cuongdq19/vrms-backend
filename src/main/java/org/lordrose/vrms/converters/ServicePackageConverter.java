package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.PackageRequest;
import org.lordrose.vrms.domains.ServicePackage;
import org.lordrose.vrms.models.responses.PackageHistoryResponse;
import org.lordrose.vrms.models.responses.ServicePackageResponse;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

    public static List<ServicePackageResponse> toServicePackageResponses(Collection<ServicePackage> servicePackages) {
        return servicePackages.stream()
                .map(ServicePackageConverter::toServicePackageResponse)
                .collect(Collectors.toList());
    }

    public static PackageHistoryResponse toPackageHistoryResponse(PackageRequest packageRequest) {
        return PackageHistoryResponse.builder()
                .packageId(packageRequest.getServicePackage().getId())
                .packageName(packageRequest.getServicePackage().getName())
                .milestone(packageRequest.getServicePackage().getMilestone())
                .sectionId(packageRequest.getServicePackage().getSection().getId())
                .sectionName(packageRequest.getServicePackage().getSection().getName())
                .build();
    }

    public static List<PackageHistoryResponse> toPackageHistoryResponses(Collection<PackageRequest> packages) {
        return packages.stream()
                .map(ServicePackageConverter::toPackageHistoryResponse)
                .collect(Collectors.toList());
    }
}
