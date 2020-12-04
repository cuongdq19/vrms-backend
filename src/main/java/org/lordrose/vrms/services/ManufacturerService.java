package org.lordrose.vrms.services;

import org.lordrose.vrms.models.responses.ManufacturerResponse;

import java.util.List;

public interface ManufacturerService {

    List<ManufacturerResponse> findAll();

    ManufacturerResponse create(String manufacturerName);
}
