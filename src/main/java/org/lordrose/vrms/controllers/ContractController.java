package org.lordrose.vrms.controllers;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.models.requests.ContactRequest;
import org.lordrose.vrms.models.requests.ManagerCreateRequest;
import org.lordrose.vrms.models.requests.ProviderRequest;
import org.lordrose.vrms.models.responses.ContractResponse;
import org.lordrose.vrms.models.responses.ProviderDetailResponse;
import org.lordrose.vrms.services.ContractService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/contracts")
public class ContractController {

    private final ContractService contractService;

    @GetMapping
    public List<ContractResponse> getAllContract() {
        return contractService.findAll();
    }

    @PostMapping("upload/{contractId}")
    public ContractResponse confirmContract(@PathVariable Long contractId,
                                            @ModelAttribute ManagerCreateRequest request,
                                            @RequestPart MultipartFile[] images) {
        return contractService.confirmContract(contractId, request, images);
    }

    @PostMapping
    public ProviderDetailResponse registerProvider(@ModelAttribute ContactRequest request,
                                                   @ModelAttribute ProviderRequest providerRequest,
                                                   @RequestPart MultipartFile[] images) {
        return contractService.registerProvider(request, providerRequest, images);
    }
}
