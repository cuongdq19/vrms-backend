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
import javax.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_service_request")
public class ServiceRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "serviceName")
    private String serviceName;

    @Column(name = "price")
    private Double price;

    @Column(name = "note")
    private String note;

    @Column(name = "is_incurred")
    private Boolean isIncurred;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private PackageRequest servicePackage;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "serviceRequest")
    private Set<ServiceRequestPart> requestParts = new LinkedHashSet<>();

    @ManyToOne
    @JoinColumn(name = "request_id")
    private Request request;

    public Long returnServiceId() {
        return service == null ? null : service.getId();
    }

    public Long returnTypeDetailId() {
        return service == null ? null : service.getTypeDetail().getId();
    }

    public Long returnTypeId() {
        return service == null ? null : service.getTypeDetail().getType().getId();
    }

    public String returnTypeName() {
        return service == null ? null : service.getTypeDetail().getType().getName();
    }

    public Long returnSectionId() {
        return service == null ? null : service.getTypeDetail().getSection().getId();
    }

    public String returnSectionName() {
        return service == null ? null : service.getTypeDetail().getSection().getName();
    }
}
