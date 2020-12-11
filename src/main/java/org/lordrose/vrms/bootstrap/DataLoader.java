package org.lordrose.vrms.bootstrap;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.Manufacturer;
import org.lordrose.vrms.domains.ModelGroup;
import org.lordrose.vrms.domains.PartCategory;
import org.lordrose.vrms.domains.PartSection;
import org.lordrose.vrms.domains.Provider;
import org.lordrose.vrms.domains.ServiceType;
import org.lordrose.vrms.domains.ServiceTypeDetail;
import org.lordrose.vrms.domains.User;
import org.lordrose.vrms.domains.Vehicle;
import org.lordrose.vrms.domains.VehicleModel;
import org.lordrose.vrms.repositories.ManufacturerRepository;
import org.lordrose.vrms.repositories.ModelGroupRepository;
import org.lordrose.vrms.repositories.PartCategoryRepository;
import org.lordrose.vrms.repositories.PartSectionRepository;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private final ModelGroupRepository groupRepository;
    private final PartSectionRepository sectionRepository;

    @Override
    public void run(String... args) throws Exception {
        //loadAll();
    }

    private void loadAll() {
        PartSection wheel = sectionRepository.save(PartSection.builder()
                .name("Banh xe")
                .build());
        PartSection brakeSystem = sectionRepository.save(PartSection.builder()
                .name("He thong phanh")
                .build());
        PartCategory rim = partCategoryRepository.save(PartCategory.builder()
                .name("Lazang")
                .section(wheel)
                .build());
        PartCategory brakePad = partCategoryRepository.save(PartCategory.builder()
                .name("Brake Pad")
                .section(brakeSystem)
                .build());

        ServiceType checkup = serviceTypeRepository.save(ServiceType.builder()
                .name("Kiem tra")
                .build());
        ServiceType replace = serviceTypeRepository.save(ServiceType.builder()
                .name("Thay the")
                .build());
        ServiceType cleanup = serviceTypeRepository.save(ServiceType.builder()
                .name("Ve sinh")
                .build());
        ServiceTypeDetail wheelCheckup = serviceTypeDetailRepository.save(ServiceTypeDetail.builder()
                .type(checkup)
                .partCategory(rim)
                .build());
        ServiceTypeDetail engineCheckup = serviceTypeDetailRepository.save(ServiceTypeDetail.builder()
                .type(checkup)
                .partCategory(brakePad)
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

        VehicleModel civic_rs_2019 = modelRepository.save(VehicleModel.builder()
                .name("Civic RS")
                .year("2019")
                .fuelType("Petrol")
                .gearbox("CVT")
                .imageUrl("image url")
                .manufacturer(honda)
                .build());
        VehicleModel civic_rs_2020 = modelRepository.save(VehicleModel.builder()
                .name("Civic RS")
                .year("2020")
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

        ModelGroup group_1 = groupRepository.save(ModelGroup.builder()
                .name("test 1")
                .description("desc")
                .provider(hondaTC)
                .models(Stream.of(civic_rs_2019).collect(Collectors.toSet()))
                .build());
        ModelGroup group_2 = groupRepository.save(ModelGroup.builder()
                .name("test 2")
                .description("desc")
                .provider(hondaTC)
                .models(Stream.of(civic_rs_2019, civic_rs_2020).collect(Collectors.toSet()))
                .build());

        Vehicle vehicle_1 = vehicleRepository.save(Vehicle.builder()
                .plateNumber("plate num")
                .vinNumber("vin num")
                .color("red")
                .boughtDate(LocalDateTime.of(2020, 12, 9, 8, 0))
                .user(owner_1)
                .model(civic_rs_2019)
                .build());

        ServiceType testType = serviceTypeRepository.findById(1L)
                .orElseThrow();
        testType.getDetails().forEach(detail -> System.out.println(
                testType.getName() + " - " + detail.getPartCategorySection() +
                        " - " + detail.getPartCategoryName()));
    }
}
