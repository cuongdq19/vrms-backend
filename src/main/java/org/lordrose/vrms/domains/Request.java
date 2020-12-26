package org.lordrose.vrms.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lordrose.vrms.domains.bases.TimeAuditable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_request")
public class Request extends TimeAuditable<LocalDateTime> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_time")
    private LocalDateTime bookingTime;

    @Column(name = "arrive_time")
    private LocalDateTime arriveTime;

    @Column(name = "checkout_time")
    private LocalDateTime checkoutTime;

    @Column(name = "note")
    private String note;

    @Column(name = "status")
    private String status;

    @Column(name = "imageUrls")
    private String imageUrls;

    @OneToMany(mappedBy = "request", fetch = FetchType.EAGER)
    private Set<PackageRequest> packages = new HashSet<>();

    @OneToMany(mappedBy = "request", fetch = FetchType.EAGER)
    private Set<ServiceRequest> services = new HashSet<>();

    @OneToMany(mappedBy = "request")
    private Set<PartRequest> parts = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "technician_id")
    private User technician;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @OneToOne(mappedBy = "request")
    private Feedback feedback;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_provider_id")
    private Provider provider;

    @OneToMany(mappedBy = "request")
    private Set<IncurredExpense> expenses = new HashSet<>();
}
