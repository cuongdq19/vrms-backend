package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.models.responses.ServiceTypeDetailResponse;
import org.lordrose.vrms.repositories.ServiceTypeDetailRepository;
import org.lordrose.vrms.services.ServiceTypeDetailService;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ServiceTypeDetailServiceImpl implements ServiceTypeDetailService {

    private final ServiceTypeDetailRepository typeDetailRepository;

    @Override
    public Object findAll(Long typeId) {
        return typeDetailRepository.findAllByTypeId(typeId).stream()
                .map(detail -> ServiceTypeDetailResponse.builder()
                        .id(detail.getId())
                        .serviceName(detail.getType().getName())
                        .sectionId(detail.getSection().getId())
                        .sectionName(detail.getSection().getName())
                        .categoryId(detail.getPartCategoryId())
                        .categoryName(detail.getPartCategoryName())
                        .build())
                .collect(Collectors.toList());
    }
}
