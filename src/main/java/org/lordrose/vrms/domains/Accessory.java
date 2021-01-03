package org.lordrose.vrms.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_accessory")
public class Accessory {

    @Id
    @Column(name = "request_part_id")
    private Long id;

    @Column(name = "duration")
    private Integer warrantyDuration;

    @Column(name = "monthsPerMaintenance")
    private Integer monthsPerMaintenance;

    @Column(name = "installed_date")
    private LocalDate installedDate;

    @OneToOne
    @MapsId
    @JoinColumn(name = "service_request_part_id")
    private ServiceRequestPart serviceRequestPart;

}
