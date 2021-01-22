package org.lordrose.vrms.converters;

import org.lordrose.vrms.constants.SuggestingValueConfig;
import org.lordrose.vrms.domains.MaintenancePackage;
import org.lordrose.vrms.domains.Provider;
import org.lordrose.vrms.domains.ServiceRequest;
import org.lordrose.vrms.exceptions.InvalidStateException;
import org.lordrose.vrms.models.responses.MaintenancePackageResponse;
import org.lordrose.vrms.models.responses.PackageCheckoutResponse;
import org.lordrose.vrms.models.responses.PackageHistoryResponse;
import org.lordrose.vrms.models.responses.PackageProviderResponse;
import org.lordrose.vrms.services.FeedbackService;
import org.lordrose.vrms.utils.distances.GeoPoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.lordrose.vrms.converters.ServiceConverter.toServiceCheckoutResponses;
import static org.lordrose.vrms.converters.ServiceConverter.toServiceDetailResponses;
import static org.lordrose.vrms.converters.ServiceConverter.toServiceHistoryResponses;
import static org.lordrose.vrms.utils.FileUrlUtils.getUrlsAsArray;
import static org.lordrose.vrms.utils.distances.DistanceCalculator.calculate;

public class MaintenancePackageConverter {

    public static MaintenancePackageResponse toMaintenancePackageResponse(MaintenancePackage maintenancePackage) {
        return MaintenancePackageResponse.builder()
                .id(maintenancePackage.getId())
                .name(maintenancePackage.getName())
                .milestone(maintenancePackage.getMilestone())
                .sectionId(maintenancePackage.returnSectionId())
                .sectionName(maintenancePackage.returnSectionName())
                .totalPrice(maintenancePackage.getPackagedServices().stream()
                        .mapToDouble(service -> service.getPrice() + service.getPartSet().stream()
                                .mapToDouble(servicePart -> servicePart.getQuantity() * servicePart.getPart().getPrice())
                                .sum())
                        .sum())
                .packagedServices(toServiceDetailResponses(maintenancePackage.getPackagedServices()))
                .build();
    }

    public static List<MaintenancePackageResponse> toMaintenancePackageResponses(Collection<MaintenancePackage> maintenancePackages) {
        return maintenancePackages.stream()
                .map(MaintenancePackageConverter::toMaintenancePackageResponse)
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

    public static List<PackageProviderResponse> toPackageProviderResponses(Collection<MaintenancePackage> maintenancePackages,
                                                                           GeoPoint currentLocation,
                                                                           FeedbackService feedbackService,
                                                                           SuggestingValueConfig suggestingValue) {
        List<PackageProviderResponse> responses = new ArrayList<>();
        Map<Provider, List<MaintenancePackage>> resultMap = maintenancePackages.stream()
                .collect(Collectors.groupingBy(maintenancePackage ->
                        maintenancePackage.getPackagedServices().stream()
                                .findAny()
                                    .orElseThrow(() -> new InvalidStateException("Something went wrong"))
                                .getProvider()));

        resultMap.forEach((provider, packages) ->
                responses.add(toPackageProviderResponses(provider, currentLocation, packages, feedbackService)));

        return responses.stream()
                .filter(provider -> provider.getDistance() <= suggestingValue.getDistanceLimit())
                .sorted(Comparator.comparingDouble(PackageProviderResponse::getDistance))
                .collect(Collectors.toList());
    }

    private static PackageProviderResponse toPackageProviderResponses(Provider provider, GeoPoint currentPos,
                                                                      List<MaintenancePackage> packages,
                                                                      FeedbackService feedbackService) {
        return PackageProviderResponse.builder()
                .id(provider.getId())
                .name(provider.getName())
                .address(provider.getAddress())
                .imageUrls(getUrlsAsArray(provider.getImageUrls()))
                .openTime(provider.getOpenTime().toString())
                .closeTime(provider.getCloseTime().toString())
                .ratings(feedbackService.getAverageRating(provider.getId()))
                .distance(calculate(currentPos, GeoPoint.builder()
                        .latitude(provider.getLatitude())
                        .longitude(provider.getLongitude())
                        .build()))
                .packages(toMaintenancePackageResponses(packages))
                .build();
    }
}
