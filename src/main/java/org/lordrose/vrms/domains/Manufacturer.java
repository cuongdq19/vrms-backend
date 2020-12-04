package org.lordrose.vrms.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_manufacturer")
public class Manufacturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "active")
    private Boolean isActive;

    @Column(name = "imageUrl")
    private String imageUrl;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "manufacturer")
    private Set<Provider> providers = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "manufacturer")
    private Set<VehicleModel> models = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "manufacturer")
    private Set<MaintenanceLevel> levels = new HashSet<>();
}
