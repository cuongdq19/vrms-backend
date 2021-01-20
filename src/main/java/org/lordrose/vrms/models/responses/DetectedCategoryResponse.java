package org.lordrose.vrms.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lordrose.vrms.models.ImagePosition;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetectedCategoryResponse {

    private Long categoryId;
    private String score;
    private ImagePosition position;
}
