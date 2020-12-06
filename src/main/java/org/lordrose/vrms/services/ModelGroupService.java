package org.lordrose.vrms.services;

import org.lordrose.vrms.models.requests.GroupRequest;
import org.lordrose.vrms.models.responses.GroupResponse;

import java.util.List;

public interface ModelGroupService {

    List<GroupResponse> findAllByProvider(Long id);

    GroupResponse create(Long providerId, GroupRequest request);

    GroupResponse updateGroupModels(Long groupId, GroupRequest request);
}
