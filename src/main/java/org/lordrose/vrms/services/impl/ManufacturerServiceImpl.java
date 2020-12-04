package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.Manufacturer;
import org.lordrose.vrms.models.responses.ManufacturerResponse;
import org.lordrose.vrms.repositories.ManufacturerRepository;
import org.lordrose.vrms.services.ManufacturerService;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.lordrose.vrms.converters.ManufacturerConverter.toManufacturerResponse;
import static org.lordrose.vrms.converters.ManufacturerConverter.toManufacturerResponses;

@RequiredArgsConstructor
@Service
public class ManufacturerServiceImpl implements ManufacturerService {

    private final ManufacturerRepository manufacturerRepository;

    @Override
    public List<ManufacturerResponse> findAll() {
        return toManufacturerResponses(manufacturerRepository.findAll());
    }

    @Override
    public ManufacturerResponse create(String manufacturerName) {
        Manufacturer saved = manufacturerRepository.save(Manufacturer.builder()
                .name(manufacturerName)
                .isActive(true)
                .imageUrl("image url")
                .build());
        return toManufacturerResponse(saved);
    }
}
