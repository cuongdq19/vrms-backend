package org.lordrose.vrms.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_service_package")
public class ServicePackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "milestone")
    private Double milestone;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private PartSection section;

    @ManyToMany
    @JoinTable(name = "tbl_package_service",
            joinColumns = @JoinColumn(name = "service_package_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    private Set<Service> packagedServices = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    private Provider provider;
}
