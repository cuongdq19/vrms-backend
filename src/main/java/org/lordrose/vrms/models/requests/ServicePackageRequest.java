package org.lordrose.vrms.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServicePackageRequest {

    private Set<Long> serviceIds;
    private Long sectionId;
    private String packageName;
    private Double milestone;
}
