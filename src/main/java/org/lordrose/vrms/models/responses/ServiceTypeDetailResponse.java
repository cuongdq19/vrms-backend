package org.lordrose.vrms.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceTypeDetailResponse {

    private Long id;
    private String typeName;
    private Long sectionId;
    private String sectionName;
    private Long categoryId;
    private String categoryName;
}