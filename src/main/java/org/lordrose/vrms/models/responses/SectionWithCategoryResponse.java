package org.lordrose.vrms.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lordrose.vrms.domains.PartCategory;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionWithCategoryResponse {

    private Long sectionId;
    private String sectionName;
    private String sectionImageUrl;
    private List<PartCategory> categories;
}
