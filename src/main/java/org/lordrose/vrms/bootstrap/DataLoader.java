package org.lordrose.vrms.bootstrap;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.Manufacturer;
import org.lordrose.vrms.domains.Provider;
import org.lordrose.vrms.domains.Role;
import org.lordrose.vrms.domains.User;
import org.lordrose.vrms.domains.Vehicle;
import org.lordrose.vrms.domains.VehicleModel;
import org.lordrose.vrms.repositories.ManufacturerRepository;
import org.lordrose.vrms.repositories.ProviderRepository;
import org.lordrose.vrms.repositories.RoleRepository;
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
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        //loadAll();
    }

    private void loadAll() {
        Role admin = roleRepository.save(Role.builder()
                .name("ADMIN")
                .description("description")
                .build());

        Role user = roleRepository.save(Role.builder()
                .name("USER")
                .description("description")
                .build());

        Role manager = roleRepository.save(Role.builder()
                .name("MANAGER")
                .description("description")
                .build());

        Role staff = roleRepository.save(Role.builder()
                .name("STAFF")
                .description("description")
                .build());
        Role tech = roleRepository.save(Role.builder()
                .name("TECHNICIAN")
                .description("description")
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

//        Provider hondaTC = providerRepository.save(Provider.builder()
//                .name("Honda")
//                .address("address")
//                .latitude(6D)
//                .longitude(9D)
//                .openTime(LocalTime.of(9,0))
//                .closeTime(LocalTime.of(17, 0))
//                .slotDuration(3)
//                .slotCapacity(1)
//                .isActive(true)
//                .imageUrls("")
//                .build());

        User admin_1 = userRepository.save(User.builder()
                .isActive(true)
                .username("admin")
                .password("admin")
                .fullName("Admin full name")
                .imageUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQgj76jmiFkhkGcQP9CffojdbmNLfZQjaGAhQ&usqp=CAU")
                .gender(true)
                .role(admin)
                .build());

        User owner_1 = userRepository.save(User.builder()
                .phoneNumber("123456")
                .password("1")
                .fullName("Duong Huynh Huynh")
                .gender(true)
                .imageUrl("image url")
                .isActive(true)
                .deviceToken("device token")
                .role(user)
                .build());

//        User mn1 = userRepository.save(User.builder()
//                .isActive(true)
//                .username("mn1")
//                .password("1")
//                .fullName("Manager 1")
//                .imageUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcS_yO9I79sxbYGB2sIGHayqoZ_uzi9mcDh65g&usqp=CAU")
//                .provider(hondaTC)
//                .gender(true)
//                .role(manager)
//                .build());

        Vehicle vehicle_1 = vehicleRepository.save(Vehicle.builder()
                .plateNumber("plate num")
                .vinNumber("vin num")
                .color("red")
                .boughtDate(LocalDateTime.of(2020, 12, 9, 8, 0))
                .user(owner_1)
                .model(civic_rs_2019)
                .build());
    }
}
