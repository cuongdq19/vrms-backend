package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.ServiceTypeDetail;
import org.lordrose.vrms.models.responses.ServiceTypeDetailResponse;
import org.lordrose.vrms.repositories.PartCategoryRepository;
import org.lordrose.vrms.repositories.ServiceTypeDetailRepository;
import org.lordrose.vrms.services.ServiceTypeDetailService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.lordrose.vrms.converters.ServiceConverter.toServiceTypeDetailResponses;

@RequiredArgsConstructor
@Service
public class ServiceTypeDetailServiceImpl implements ServiceTypeDetailService {

    private final ServiceTypeDetailRepository typeDetailRepository;
    private final PartCategoryRepository categoryRepository;

    @Override
    public Object findAll(Set<Long> typeIds) {
        List<ServiceTypeDetail> result = new ArrayList<>();

        typeIds.forEach(typeId -> result.addAll(typeDetailRepository.findAllByTypeId(typeId)));

        return result.stream()
                .map(detail -> ServiceTypeDetailResponse.builder()
                        .id(detail.getId())
                        .typeName(detail.getType().getName())
                        .sectionId(detail.getSection().getId())
                        .sectionName(detail.getSection().getName())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public Object findAllServiceTypeSections() {
        return toServiceTypeDetailResponses(typeDetailRepository.findAll());
    }

    @Override
    public Object findAllCategories(Long sectionId) {
        return categoryRepository.findAllBySectionId(sectionId);
    }
}
