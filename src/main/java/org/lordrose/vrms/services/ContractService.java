package org.lordrose.vrms.services;

import org.lordrose.vrms.models.requests.ContactRequest;
import org.lordrose.vrms.models.requests.ManagerCreateRequest;
import org.lordrose.vrms.models.requests.ProviderRequest;
import org.springframework.web.multipart.MultipartFile;

public interface ContractService {

    Object findAll();

    Object registerProvider(ContactRequest request, MultipartFile[] images);

    Object confirmContract(Long contractId, MultipartFile[] contractImages);

    Object resolvedContract(Long contractId, ProviderRequest providerRequest,
                            ManagerCreateRequest managerRequest, MultipartFile[] images);
}
