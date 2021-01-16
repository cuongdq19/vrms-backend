package org.lordrose.vrms.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SuggestingValueConfig {

    @Value("${distance.limit.criteria}")
    private Integer distanceLimit;

    public Integer getDistanceLimit() {
        if (distanceLimit == null)
            return 50;
        return distanceLimit;
    }
}
