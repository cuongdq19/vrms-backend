package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.PartSection;
import org.lordrose.vrms.domains.ServiceType;
import org.lordrose.vrms.domains.ServiceTypeDetail;
import org.lordrose.vrms.models.responses.ServiceTypeDetailResponse;
import org.lordrose.vrms.repositories.ServiceTypeDetailRepository;
import org.lordrose.vrms.repositories.ServiceTypeRepository;
import org.lordrose.vrms.services.ServiceTypeDetailService;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.lordrose.vrms.exceptions.ResourceNotFoundException.newExceptionWithValue;

@RequiredArgsConstructor
@Service
public class ServiceTypeDetailServiceImpl implements ServiceTypeDetailService {

    private final ServiceTypeDetailRepository typeDetailRepository;
    private final ServiceTypeRepository typeRepository;

    @Override
    public Object findAll(Long typeId) {
        return typeDetailRepository.findAllByTypeId(typeId).stream()
                .map(detail -> ServiceTypeDetailResponse.builder()
                        .id(detail.getId())
                        .typeName(detail.getType().getName())
                        .sectionId(detail.getSection().getId())
                        .sectionName(detail.getSection().getName())
                        .categoryId(detail.getPartCategoryId())
                        .categoryName(detail.getPartCategoryName())
                        .build())
                .collect(Collectors.toList());
    }

    public Object findAllSectionReplaced() {
        ServiceType replace = typeRepository.findByName("Thay thế và lắp ráp")
                .orElseThrow(() -> newExceptionWithValue("Thay thế và lắp ráp"));
        List<ServiceTypeDetail> details = typeDetailRepository.findAllByTypeId(replace.getId());
        Map<PartSection, List<ServiceTypeDetail>> bySection = new LinkedHashMap<>(details.stream()
                .collect(Collectors.groupingBy(ServiceTypeDetail::getSection)));
        return bySection.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().getName(),
                        e -> e.getValue().stream()
                                .map(ServiceTypeDetail::getPartCategory)));
    }
}
