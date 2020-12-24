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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_provider")
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "open_time")
    private LocalTime openTime;

    @Column(name = "close_time")
    private LocalTime closeTime;

    @Column(name = "slot_duration")
    private Integer slotDuration;

    @Column(name = "capacity_per_slot")
    private Integer slotCapacity;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "imageUrls", length = 5000)
    private String imageUrls;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "provider")
    private Set<User> users = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "provider")
    private Set<VehiclePart> parts = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "provider")
    private Set<Request> requests = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "contract_id", referencedColumnName = "id")
    private Contract contract;

    public Long getManufacturerId() {
        return this.getManufacturer() == null ?
                null : this.getManufacturer().getId();
    }

    public String getManufacturerName() {
        return this.getManufacturer() == null ?
                "Have no manufacturer" : this.getManufacturer().getName();
    }
}
