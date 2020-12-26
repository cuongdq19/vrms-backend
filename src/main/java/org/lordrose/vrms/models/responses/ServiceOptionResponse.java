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
public class ServiceOptionResponse {

    private Long id;
    private String name;
    private Double price;
    private List<PartQuantityResponse> parts;
    private Double total;
}
