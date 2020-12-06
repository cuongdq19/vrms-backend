package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.MaintenanceLevelDetail;
import org.lordrose.vrms.models.responses.LevelDetailResponse;

public class MaintenanceLevelDetailConverter {

    public static LevelDetailResponse toLevelDetailResponse(MaintenanceLevelDetail detail) {
        return LevelDetailResponse.builder().build();
    }
}
