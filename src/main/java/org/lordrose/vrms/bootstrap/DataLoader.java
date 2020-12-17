package org.lordrose.vrms.bootstrap;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.repositories.BasePackageRepository;
import org.lordrose.vrms.repositories.ProviderRepository;
import org.lordrose.vrms.repositories.ServicePackageRepository;
import org.lordrose.vrms.repositories.ServiceTypeDetailRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {

    private final ProviderRepository providerRepository;
    private final ServicePackageRepository servicePackageRepository;
    private final BasePackageRepository basePackageRepository;
    private final ServiceTypeDetailRepository typeDetailRepository;

    @Override
    public void run(String... args) {
//        BasePackage five_km = basePackageRepository.save(BasePackage.builder()
//                .name("5000 km")
//                .description("Bao duong 5000 km")
//                .typeDetails(new HashSet<>(typeDetailRepository.findAllById(
//                        List.of(27L, 122L, 147L))))
//                .build());
//        BasePackage ten_km = basePackageRepository.save(BasePackage.builder()
//                .name("10000 km")
//                .description("Bao duong 10000 km")
//                .typeDetails(new HashSet<>(typeDetailRepository.findAllById(
//                        List.of(27L, 122L, 147L, 148L))))
//                .build());
//        BasePackage twenty_km = basePackageRepository.save(BasePackage.builder()
//                .name("20000 km")
//                .description("Bao duong 20000 km")
//                .typeDetails(new HashSet<>(typeDetailRepository.findAllById(
//                        List.of(27L, 122L, 138L, 139L, 148L, 149L, 150L))))
//                .build());
//        BasePackage forty_km = basePackageRepository.save(BasePackage.builder()
//                .name("40000 km")
//                .description("Bao duong 40000 km")
//                .typeDetails(new HashSet<>(typeDetailRepository.findAllById(
//                        List.of(27L, 122L, 125L, 138L, 139L, 147L, 148L, 149L, 150L, 151L))))
//                .build());
//
//        ServicePackage servicePackage_1 = servicePackageRepository.save(ServicePackage.builder()
//                .basePackage(five_km)
//                .provider(providerRepository.findById(1L)
//                        .orElseThrow())
//                .build());
//        ServicePackage servicePackage_2 = servicePackageRepository.save(ServicePackage.builder()
//                .basePackage(ten_km)
//                .provider(providerRepository.findById(1L)
//                        .orElseThrow())
//                .build());
//        ServicePackage servicePackage_3 = servicePackageRepository.save(ServicePackage.builder()
//                .basePackage(twenty_km)
//                .provider(providerRepository.findById(1L)
//                        .orElseThrow())
//                .build());
//        ServicePackage servicePackage_4 = servicePackageRepository.save(ServicePackage.builder()
//                .basePackage(forty_km)
//                .provider(providerRepository.findById(1L)
//                        .orElseThrow())
//                .build());
//
//        List<ServicePackage> packages = List.of(servicePackage_1, servicePackage_2,
//                servicePackage_3, servicePackage_4);
//        packages.forEach(servicePackage -> {
//            System.out.println(
//                    servicePackage.getProvider().getName() + ": " + servicePackage.getBasePackage().getName()
//            );
//            servicePackage.getBasePackage().getTypeDetails().forEach(detail -> {
//                System.out.println("\t" + detail.getServiceTypeDetailName());
//            });
//            System.out.println("------");
//        });
    }
}
