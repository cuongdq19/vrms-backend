package org.lordrose.vrms.services;

public interface ServiceTypeDetailService {

    Object findAll(Long typeId);

    Object findAllSectionReplaced();

    Object findAllCategories();
}
