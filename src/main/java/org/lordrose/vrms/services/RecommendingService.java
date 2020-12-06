package org.lordrose.vrms.services;

import org.lordrose.vrms.models.responses.ProviderDistanceResponse;
import org.lordrose.vrms.utils.distances.GeoPoint;

import java.util.List;

public interface RecommendingService {

    List<ProviderDistanceResponse> recommendProviders(GeoPoint currentPos);
}
