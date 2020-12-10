package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.converters.ContractConverter;
import org.lordrose.vrms.domains.Contract;
import org.lordrose.vrms.domains.Manufacturer;
import org.lordrose.vrms.domains.Provider;
import org.lordrose.vrms.domains.User;
import org.lordrose.vrms.exceptions.InvalidArgumentException;
import org.lordrose.vrms.models.requests.ContactRequest;
import org.lordrose.vrms.models.requests.ManagerCreateRequest;
import org.lordrose.vrms.models.requests.ProviderRequest;
import org.lordrose.vrms.models.responses.ContractResponse;
import org.lordrose.vrms.models.responses.ProviderDetailResponse;
import org.lordrose.vrms.repositories.ContractRepository;
import org.lordrose.vrms.repositories.ManufacturerRepository;
import org.lordrose.vrms.repositories.ProviderRepository;
import org.lordrose.vrms.repositories.RoleRepository;
import org.lordrose.vrms.repositories.UserRepository;
import org.lordrose.vrms.services.ContractService;
import org.lordrose.vrms.services.EmailService;
import org.lordrose.vrms.services.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.lordrose.vrms.converters.ContractConverter.toContractResponses;
import static org.lordrose.vrms.converters.ProviderConverter.toProviderDetailResponse;
import static org.lordrose.vrms.exceptions.ResourceNotFoundException.newExceptionWithId;
import static org.lordrose.vrms.exceptions.ResourceNotFoundException.newExceptionWithValue;
import static org.lordrose.vrms.utils.DateTimeUtils.toLocalTime;
import static org.lordrose.vrms.utils.PasswordUtils.getRandomAlphabeticWithLength;

@RequiredArgsConstructor
@Service
public class ContractServiceImpl implements ContractService {

    private final StorageService storageService;
    private final EmailService emailService;
    private final ContractRepository contractRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final ProviderRepository providerRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public List<ContractResponse> findAll() {
        return toContractResponses(contractRepository.findAll());
    }

    @Override
    public ProviderDetailResponse registerProvider(ContactRequest contactRequest,
                                                   ProviderRequest providerRequest,
                                                   MultipartFile[] images) {
        Contract contract = contractRepository.save(Contract.builder()
                .fullName(contactRequest.getFullName())
                .address(contactRequest.getAddress())
                .phoneNumber(contactRequest.getPhoneNumber())
                .email(contactRequest.getEmail())
                .status("PENDING")
                .build());
        Manufacturer manufacturer = manufacturerRepository.findById(providerRequest.getManufacturerId())
                .orElse(null);
        Provider saved = providerRepository.save(Provider.builder()
                .name(providerRequest.getProviderName())
                .address(providerRequest.getAddress())
                .latitude(providerRequest.getLatitude())
                .longitude(providerRequest.getLongitude())
                .openTime(toLocalTime(providerRequest.getOpenTime()))
                .closeTime(toLocalTime(providerRequest.getCloseTime()))
                .slotDuration(providerRequest.getSlotDuration())
                .slotCapacity(providerRequest.getSlotCapacity())
                .isActive(false)
                .imageUrls(storageService.uploadFiles(images))
                .manufacturer(manufacturer)
                .contract(contract)
                .build());
        return toProviderDetailResponse(saved);
    }

    @Override
    public ContractResponse confirmContract(Long contractId, ManagerCreateRequest request,
                                            MultipartFile[] images) {
        userRepository.findUserByUsername(request.getUsername())
                .ifPresent(user -> {
                    throw new InvalidArgumentException("User " + user.getUsername() +
                            " is already existed!");
                });

        Contract result = contractRepository.findById(contractId)
                .orElseThrow(() -> newExceptionWithId(contractId));

        result.setImageUrls(storageService.uploadFiles(images));
        result.setStatus("CONFIRMED");
        result.getProvider().setIsActive(true);

        Contract saved = contractRepository.save(result);
        Provider provider = providerRepository.save(saved.getProvider());

        final String password = getRandomAlphabeticWithLength(8);

        User manager = userRepository.save(User.builder()
                .username(request.getUsername())
                .password(password)
                .fullName(request.getFullName())
                .gender(request.getGender())
                .imageUrl("default")
                .isActive(true)
                .role(roleRepository.findByNameIgnoreCase("MANAGER")
                        .orElseThrow(() -> newExceptionWithValue("MANAGER")))
                .provider(provider)
                .build());

        final String subject = "Your Service Provider registration is approved.";
        final String text = "Your contract is approved by our system. " +
                "Your account is display below. " +
                "Please sign in and change your password for security purposes." +
                "Account info: \n" +
                "Full name: " + manager.getFullName() + "\n" +
                "Gender: " + (manager.getGender() ? "Male" : "Female") + "\n" +
                "Username: " + manager.getUsername() + "\n" +
                "Password: " + password;

        emailService.sendMail(result.getEmail(), subject, text);

        return ContractConverter.toContractResponse(saved);
    }
}
