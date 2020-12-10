package org.lordrose.vrms.bootstrap;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.Manufacturer;
import org.lordrose.vrms.domains.PartCategory;
import org.lordrose.vrms.domains.Provider;
import org.lordrose.vrms.domains.ServiceType;
import org.lordrose.vrms.domains.ServiceTypeDetail;
import org.lordrose.vrms.domains.User;
import org.lordrose.vrms.domains.Vehicle;
import org.lordrose.vrms.domains.VehicleModel;
import org.lordrose.vrms.repositories.ManufacturerRepository;
import org.lordrose.vrms.repositories.PartCategoryRepository;
import org.lordrose.vrms.repositories.ProviderRepository;
import org.lordrose.vrms.repositories.ServiceTypeDetailRepository;
import org.lordrose.vrms.repositories.ServiceTypeRepository;
import org.lordrose.vrms.repositories.UserRepository;
import org.lordrose.vrms.repositories.VehicleModelRepository;
import org.lordrose.vrms.repositories.VehicleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;

@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {

    private final ManufacturerRepository manufacturerRepository;
    private final VehicleModelRepository modelRepository;
    private final ProviderRepository providerRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final ServiceTypeDetailRepository serviceTypeDetailRepository;
    private final PartCategoryRepository partCategoryRepository;

    @Override
    public void run(String... args) throws Exception {
        loadAll();
    }

    private void loadAll() {
        PartCategory wheel = partCategoryRepository.save(PartCategory.builder()
                .name("Banh xe")
                .build());

        ServiceType checkup = serviceTypeRepository.save(ServiceType.builder()
                .name("Kiem tra")
                .build());
        ServiceTypeDetail wheelCheckup = serviceTypeDetailRepository.save(ServiceTypeDetail.builder()
                .name("Kiem tra banh xe")
                .type(checkup)
                .partCategory(wheel)
                .build());
        ServiceTypeDetail engineCheckup = serviceTypeDetailRepository.save(ServiceTypeDetail.builder()
                .name("Dong Co")
                .type(checkup)
                .build());
        User owner_1 = userRepository.save(User.builder()
                .phoneNumber("123456")
                .password("1")
                .fullName("Duong Huynh Huynh")
                .gender(true)
                .imageUrl("image url")
                .isActive(true)
                .deviceToken("device token")
                .build());
        Manufacturer honda = manufacturerRepository.save(Manufacturer.builder()
                .name("Honda")
                .isActive(true)
                .imageUrl("image url")
                .build());

        VehicleModel civic_rs = modelRepository.save(VehicleModel.builder()
                .name("Civic RS")
                .year("2019")
                .fuelType("Petrol")
                .gearbox("CVT")
                .imageUrl("image url")
                .manufacturer(honda)
                .build());

        Provider hondaTC = providerRepository.save(Provider.builder()
                .name("Honda")
                .address("address")
                .latitude(6D)
                .longitude(9D)
                .openTime(LocalTime.of(9,0))
                .closeTime(LocalTime.of(17, 0))
                .slotDuration(3)
                .slotCapacity(1)
                .isActive(true)
                .imageUrls("")
                .build());

        Vehicle vehicle_1 = vehicleRepository.save(Vehicle.builder()
                .plateNumber("plate num")
                .vinNumber("vin num")
                .color("red")
                .boughtDate(LocalDateTime.of(2020, 12, 9, 8, 0))
                .user(owner_1)
                .model(civic_rs)
                .build());

        ServiceType testType = serviceTypeRepository.findById(1L)
                .orElseThrow();
        testType.getDetails().forEach(detail -> System.out.println(
                testType.getName() + " - " + detail.getName() +
                        " - " + detail.getPartCategoryName()));
    }
}
