package org.lordrose.vrms.bootstrap;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.models.requests.ExpenseRequest;
import org.lordrose.vrms.models.requests.GroupPriceRequest;
import org.lordrose.vrms.models.requests.MaintenancePackageRequest;
import org.lordrose.vrms.models.requests.RequestIncurredUpdateRequest;
import org.lordrose.vrms.models.requests.RequestInfoRequest;
import org.lordrose.vrms.models.requests.ServiceInfoRequest;
import org.lordrose.vrms.models.requests.ServiceNonReplacingInfoRequest;
import org.lordrose.vrms.models.responses.RequestCheckOutResponse;
import org.lordrose.vrms.services.MaintenancePackageService;
import org.lordrose.vrms.services.RequestService;
import org.lordrose.vrms.services.ServiceProcessingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {

    private final ServiceProcessingService processingService;
    private final RequestService requestService;
    private final MaintenancePackageService packageProcessingService;

    @Override
    public void run(String... args) {
        /*replacingService_1();
        non_replacingService();
        replacingService_2();
        createPackage_1();
        createPackage_2();
        createPackage_3();
        createRequest();
        createRequest();*/

        RequestCheckOutResponse temp1 = incurredUpdateRequest();

        System.out.println("CBA");
    }

    private void replacingService_1() {
        Map<Long, Double> partMap = new LinkedHashMap<>();
        partMap.put(1L, 1D);
        GroupPriceRequest temp = new GroupPriceRequest();
        temp.setName("Replacing Service 1");
        temp.setPrice(69D);
        temp.setPartQuantity(partMap);
        ServiceInfoRequest request = new ServiceInfoRequest();
        request.setGroupPriceRequest(temp);
        request.setTypeDetailId(1L);
        processingService.create(1L, request);
    }

    private void non_replacingService() {
        ServiceNonReplacingInfoRequest request = new ServiceNonReplacingInfoRequest();
        request.setServiceName("Non Replacing Service");
        request.setPrice(420D);
        request.setModelIds(Set.of(1L, 2L));
        request.setTypeDetailId(1L);
        processingService.create(1L, request);
    }

    private void replacingService_2() {
        Map<Long, Double> partMap = new LinkedHashMap<>();
        partMap.put(4L, 1D);
        partMap.put(5L, 2D);
        partMap.put(6L, 3D);
        GroupPriceRequest temp = new GroupPriceRequest();
        temp.setName("Replacing Service 2");
        temp.setPrice(96D);
        temp.setPartQuantity(partMap);
        ServiceInfoRequest request = new ServiceInfoRequest();
        request.setGroupPriceRequest(temp);
        request.setTypeDetailId(1L);
        processingService.create(1L, request);
    }

    private void createPackage_1() {
        MaintenancePackageRequest request = new MaintenancePackageRequest();
        request.setServiceIds(List.of(1L, 2L));
        request.setPackageName("Test Maintenance Package Name 1");
        request.setMilestoneId(1);
        packageProcessingService.create(1L, request);
    }

    private void createPackage_2() {
        MaintenancePackageRequest request = new MaintenancePackageRequest();
        request.setServiceIds(List.of(2L, 3L));
        request.setPackageName("Test Maintenance Package Name 2");
        request.setSectionId(1L);
        packageProcessingService.create(1L, request);
    }

    private void createPackage_3() {
        MaintenancePackageRequest request = new MaintenancePackageRequest();
        request.setServiceIds(List.of(1L, 3L));
        request.setPackageName("Test Maintenance Package Name 3");
        request.setSectionId(3L);
        packageProcessingService.create(1L, request);
    }

    private void createRequest() {
        RequestInfoRequest request = new RequestInfoRequest();
        request.setBookingTime(1609459199L);
        request.setNote("Test note");
        request.setVehicleId(1L);
        request.setServiceIds(List.of(1L, 2L));
        request.setPackageIds(List.of(1L, 2L));
        request.setProviderId(1L);
        requestService.create(request);
    }

    private RequestCheckOutResponse incurredUpdateRequest() {
        // Packages
        Map<Long, Map<Long, Map<Long, Double>>> packageMap = new LinkedHashMap<>();
        Map<Long, Map<Long, Double>> packageServiceMap_1 = new LinkedHashMap<>();
        Map<Long, Double> packageServicePartMap_1 = new LinkedHashMap<>();
        packageServicePartMap_1.put(1L, 1.0D);
        packageServicePartMap_1.put(8L, 8.0D);
        packageServiceMap_1.put(1L, packageServicePartMap_1);
        packageMap.put(1L, packageServiceMap_1);
        Map<Long, Map<Long, Double>> packageServiceMap_2 = new LinkedHashMap<>();
        Map<Long, Double> packageServicePartMap_2 = new LinkedHashMap<>();
        packageServicePartMap_2.put(2L, 2.0D);
        packageServicePartMap_2.put(9L, 9.0D);
        packageServiceMap_2.put(2L, packageServicePartMap_2);
        packageMap.put(2L, packageServiceMap_2);

        // Services
        Map<Long, Double> map1 = new LinkedHashMap<>();
        map1.put(1L, 1.0D);
        map1.put(2L, 2.0D);
        Map<Long, Double> map2 = new LinkedHashMap<>();
        map2.put(4L, 4.0D);
        map2.put(5L, 5.0D);
        map2.put(6L, 6.0D);
        Map<Long, Map<Long, Double>> serviceMap = new LinkedHashMap<>();
        serviceMap.put(1L, map1);
        serviceMap.put(3L, map2);

        // Expenses
        Set<ExpenseRequest> expenses = new LinkedHashSet<>();
        ExpenseRequest expense1 = new ExpenseRequest();
        expense1.setName("Expense 1");
        expense1.setPrice(11D);
        expense1.setNote("Test note expense 1");
        Map<Long, Double> partMap = new LinkedHashMap<>();
        partMap.put(1L, 6D);
        partMap.put(2L, 9D);
        expense1.setParts(partMap);
        expenses.add(expense1);
        ExpenseRequest expense2 = new ExpenseRequest();
        expense2.setName("Expense 2");
        expense2.setPrice(22D);
        expense2.setNote("Test note expense 2");
        Map<Long, Double> partMap2 = new LinkedHashMap<>();
        partMap2.put(3L, 16D);
        partMap2.put(4L, 19D);
        expense2.setParts(partMap2);
        ExpenseRequest expense3 = new ExpenseRequest();
        expense3.setMaintenancePackageId(1L);
        expense3.setName("Expense 3 (in maintenance)");
        expense3.setPrice(33D);
        expense3.setNote("Test note expense 3 (in maintenance)");
        Map<Long, Double> partMap3 = new LinkedHashMap<>();
        partMap3.put(3L, 16D);
        partMap3.put(4L, 19D);
        expense3.setParts(partMap3);
        expenses.add(expense1);
        expenses.add(expense2);
        expenses.add(expense3);
        RequestIncurredUpdateRequest request = new RequestIncurredUpdateRequest();
        request.setServicePartMap(serviceMap);
        request.setExpenses(expenses);
        request.setPackageMap(packageMap);
        return requestService.update(1L, request);
    }
}
