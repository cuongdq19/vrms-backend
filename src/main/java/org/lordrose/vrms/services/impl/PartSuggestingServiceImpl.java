package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.repositories.VehiclePartRepository;
import org.lordrose.vrms.services.PartSuggestingService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PartSuggestingServiceImpl implements PartSuggestingService {

    private final VehiclePartRepository partRepository;

    @Override
    public Object findAllByCategory(Long categoryId) {
        return null;
    }
}
