package org.lordrose.vrms.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lordrose.vrms.domains.bases.TimeAuditable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_service_vehicle_part")
public class ServiceVehiclePart extends TimeAuditable<LocalDateTime> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity")
    private Double quantity;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "vehicle_part_id", nullable = false)
    private VehiclePart part;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;
}
