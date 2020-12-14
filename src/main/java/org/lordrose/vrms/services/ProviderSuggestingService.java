package org.lordrose.vrms.services;

import org.lordrose.vrms.models.requests.FindProviderWithCategoryRequest;
import org.lordrose.vrms.models.requests.FindProviderWithServicesRequest;

public interface ProviderSuggestingService {

    Object findProviders(FindProviderWithServicesRequest request);

    Object findProviders(FindProviderWithCategoryRequest request);
}
