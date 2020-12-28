package org.lordrose.vrms.services;

import java.util.Set;

public interface ServiceTypeDetailService {

    Object findAll(Set<Long> typeIds);

    Object findAllServiceTypeSections();

    Object findAllCategories(Long sectionId);

    Object findAllSectionsWithCategory();
}
