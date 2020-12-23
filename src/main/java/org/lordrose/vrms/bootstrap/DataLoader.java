package org.lordrose.vrms.bootstrap;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {

    /*private final ProviderRepository providerRepository;
    private final ServicePackageRepository servicePackageRepository;
    private final ServiceRepository serviceRepository;*/

    @Override
    public void run(String... args) {
        //testPackage();
    }

    /*private void testPackage() {
        Set<Service> temps = new HashSet<>(serviceRepository.findAllByProviderIdAndTypeDetailType_Id(1L, 1L));
        ServicePackage servicePackage_1 = servicePackageRepository.save(ServicePackage.builder()
                .name("Cap 1")
                .milestone(5000D)
                .section(null)
                .packagedServices(temps)
                .provider(providerRepository.findById(1L)
                        .orElseThrow())
                .build());
        temps = new HashSet<>(serviceRepository.findAllByProviderIdAndTypeDetailType_Id(1L, 2L));
        ServicePackage servicePackage_2 = servicePackageRepository.save(ServicePackage.builder()
                .name("Cap 2")
                .milestone(10000D)
                .section(null)
                .packagedServices(temps)
                .provider(providerRepository.findById(1L)
                        .orElseThrow())
                .build());
        temps = new HashSet<>(serviceRepository.findAllByProviderIdAndTypeDetailType_Id(1L, 3L));
        ServicePackage servicePackage_3 = servicePackageRepository.save(ServicePackage.builder()
                .name("Cap 3")
                .milestone(20000D)
                .section(null)
                .packagedServices(temps)
                .provider(providerRepository.findById(1L)
                        .orElseThrow())
                .build());
        temps = new HashSet<>(serviceRepository.findAllByProviderIdAndTypeDetailType_Id(1L, 1L));
        temps.addAll(serviceRepository.findAllByProviderIdAndTypeDetailType_Id(1L, 2L));
        temps.addAll(serviceRepository.findAllByProviderIdAndTypeDetailType_Id(1L, 3L));
        ServicePackage servicePackage_4 = servicePackageRepository.save(ServicePackage.builder()
                .name("Cap 4 cap cuoi")
                .milestone(40000D)
                .section(null)
                .packagedServices(temps)
                .provider(providerRepository.findById(1L)
                        .orElseThrow())
                .build());
        temps = new HashSet<>(serviceRepository.findAllById(List.of(32L, 31L, 30L, 29L)));
        ServicePackage servicePackage_5 = servicePackageRepository.save(ServicePackage.builder()
                .name("Bao duong dong co")
                .milestone(null)
                .packagedServices(temps)
                .provider(providerRepository.findById(1L)
                        .orElseThrow())
                .build());
        List<ServicePackage> packages = List.of(servicePackage_1, servicePackage_2,
                servicePackage_3, servicePackage_4);
        packages.forEach(servicePackage -> {
            System.out.println(
                    servicePackage.getProvider().getName() + ": " + servicePackage.getMilestone() + "km"
            );
            servicePackage.getPackagedServices().forEach(service ->
                    System.out.println(service.getName() + " - " +
                        service.getTypeDetail().getType().getName() + " - " +
                        service.getTypeDetail().getSection().getName()));
            System.out.println("------");
        });
    }*/
}
