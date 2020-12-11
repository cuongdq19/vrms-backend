package org.lordrose.vrms.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_service_type_detail")
public class ServiceTypeDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private ServiceType type;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "part_category_id")
    private PartCategory partCategory;

    public Long getPartCategoryId() {
        return partCategory == null ? null : partCategory.getId();
    }

    public String getPartCategoryName() {
        return partCategory == null ? "" : partCategory.getName();
    }

    public String getPartCategorySection() {
        return partCategory == null ? "" : partCategory.getSection().getName();
    }
}
