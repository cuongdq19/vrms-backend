package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.models.responses.ServiceTypeResponse;
import org.lordrose.vrms.repositories.ServiceTypeRepository;
import org.lordrose.vrms.services.ServiceTypeService;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ServiceTypeServiceImpl implements ServiceTypeService {

    private final ServiceTypeRepository typeRepository;

    @Override
    public Object findAll() {
        return typeRepository.findAll().stream()
                .map(type -> ServiceTypeResponse.builder()
                        .id(type.getId())
                        .name(type.getName())
                        .build())
                .collect(Collectors.toList());
    }
}
