package org.lordrose.vrms.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lordrose.vrms.utils.distances.GeoPoint;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProviderMaintenanceRequest {

    private Set<Long> sectionIds;
    private GeoPoint currentLocation;
}
