package org.lordrose.vrms.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lordrose.vrms.domains.bases.TimeAuditable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_vehicle_part")
public class VehiclePart extends TimeAuditable<LocalDateTime> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "image_urls", length = 5000)
    private String imageUrls;

    @Column(name = "warranty_duration")
    private Integer warrantyDuration;

    @Column(name = "months_per_maintenance")
    private Integer monthsPerMaintenance;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private Provider provider;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private PartCategory category;

    @OneToMany(mappedBy = "service")
    private Set<ServiceVehiclePart> serviceParts = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "tbl_vehicle_part_model",
            joinColumns = @JoinColumn(name = "part_id"),
            inverseJoinColumns = @JoinColumn(name = "model_id"))
    private Set<VehicleModel> models = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehiclePart that = (VehiclePart) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
