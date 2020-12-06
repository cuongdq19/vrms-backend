package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.User;
import org.lordrose.vrms.models.responses.TechnicianResponse;

public class TechnicianConverter {
    public static TechnicianResponse toTechnicianResponse(User user) {
        return TechnicianResponse.builder()
                .build();
    }
}
