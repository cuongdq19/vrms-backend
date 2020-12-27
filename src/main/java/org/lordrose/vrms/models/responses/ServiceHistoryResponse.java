package org.lordrose.vrms.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceHistoryResponse {

    private Long id;
    private Double price;
    private String serviceName;
    private Long typeDetailId;
    private Long typeId;
    private String typeName;
    private Long sectionId;
    private String sectionName;
    private List<PartHistoryResponse> parts;
}
