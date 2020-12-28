package org.lordrose.vrms.controllers;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.services.ServiceTypeDetailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/service-type-details")
public class ServiceTypeDetailController {

    private final ServiceTypeDetailService typeDetailService;

    @PostMapping
    public Object findAll(@RequestBody Set<Long> typeIds) {
        return typeDetailService.findAll(typeIds);
    }

    @GetMapping("/sections")
    public Object findAllServiceTypeSections() {
        return typeDetailService.findAllServiceTypeSections();
    }

    @GetMapping("/categories/sections/{sectionId}")
    public Object findAllCategoriesBySection(@PathVariable Long sectionId) {
        return typeDetailService.findAllCategories(sectionId);
    }

    @GetMapping("/sections/categories")
    public Object findAllSectionsWithCategory() {
        return typeDetailService.findAllSectionsWithCategory();
    }
}
