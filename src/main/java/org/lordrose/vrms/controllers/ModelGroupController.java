package org.lordrose.vrms.controllers;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.models.requests.GroupRequest;
import org.lordrose.vrms.models.responses.GroupResponse;
import org.lordrose.vrms.services.ModelGroupService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/groups")
public class ModelGroupController {

    private final ModelGroupService groupService;

    @GetMapping("/providers/{id}")
    public List<GroupResponse> getAllByProvider(@PathVariable Long id) {
        return groupService.findAllByProvider(id);
    }

    @PostMapping("/providers/{id}")
    public GroupResponse create(@PathVariable Long id,
                                @RequestBody GroupRequest request) {
        return groupService.create(id, request);
    }

    @PostMapping("/{id}")
    public GroupResponse updateGroup(@PathVariable Long id,
                                     @RequestBody GroupRequest request) {
        return groupService.updateGroupModels(id, request);
    }
}
