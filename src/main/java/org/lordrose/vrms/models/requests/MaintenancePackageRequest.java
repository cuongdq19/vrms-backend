package org.lordrose.vrms.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaintenancePackageRequest {

    private String packageName;
    private Long sectionId;
    private Integer milestoneId;
    private List<Long> serviceIds;
}
