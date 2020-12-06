package org.lordrose.vrms.services;

import org.lordrose.vrms.models.requests.ContactRequest;
import org.lordrose.vrms.models.requests.ManagerCreateRequest;
import org.lordrose.vrms.models.requests.ProviderRequest;
import org.lordrose.vrms.models.responses.ContractResponse;
import org.lordrose.vrms.models.responses.ProviderDetailResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ContractService {

    List<ContractResponse> findAll();

    ContractResponse confirmContract(Long contractId, ManagerCreateRequest request,
                                     MultipartFile[] images);

    ProviderDetailResponse registerProvider(ContactRequest request,
                                            ProviderRequest providerRequest,
                                            MultipartFile[] images);
}
