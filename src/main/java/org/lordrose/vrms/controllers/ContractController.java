package org.lordrose.vrms.controllers;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.models.requests.ContactRequest;
import org.lordrose.vrms.models.requests.ManagerCreateRequest;
import org.lordrose.vrms.models.requests.ProviderRequest;
import org.lordrose.vrms.services.ContractService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/contracts")
public class ContractController {

    private final ContractService contractService;

    @GetMapping
    public Object getAllContract() {
        return contractService.findAll();
    }

    @PostMapping
    public Object registerProvider(@ModelAttribute ContactRequest request,
                                   @RequestPart MultipartFile[] images) {
        return contractService.registerProvider(request, images);
    }

    @PostMapping("/confirm/{contractId}")
    public Object confirmContract(@PathVariable Long contractId,
                                  @RequestPart MultipartFile[] images) {
        return contractService.confirmContract(contractId, images);
    }

    @GetMapping("/deny/{contractId}")
    public Object denyContract(@PathVariable Long contractId) {
        return contractService.denyContract(contractId);
    }

    @PostMapping("/resolve/{contractId}")
    public Object resolvedContract(@PathVariable Long contractId,
                                   @ModelAttribute ProviderRequest providerRequest,
                                   @ModelAttribute ManagerCreateRequest managerRequest,
                                   @RequestPart MultipartFile[] images) {
        return contractService.resolvedContract(contractId, providerRequest,
                managerRequest, images);
    }
}
