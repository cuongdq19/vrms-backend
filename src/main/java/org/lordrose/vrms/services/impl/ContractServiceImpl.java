package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.Contract;
import org.lordrose.vrms.domains.Provider;
import org.lordrose.vrms.domains.User;
import org.lordrose.vrms.domains.constants.ContractStatus;
import org.lordrose.vrms.exceptions.InvalidArgumentException;
import org.lordrose.vrms.models.requests.ContactRequest;
import org.lordrose.vrms.models.requests.ManagerCreateRequest;
import org.lordrose.vrms.models.requests.ProviderRequest;
import org.lordrose.vrms.models.responses.ContractResponse;
import org.lordrose.vrms.repositories.ContractRepository;
import org.lordrose.vrms.repositories.ProviderRepository;
import org.lordrose.vrms.repositories.RoleRepository;
import org.lordrose.vrms.repositories.UserRepository;
import org.lordrose.vrms.services.ContractService;
import org.lordrose.vrms.services.EmailService;
import org.lordrose.vrms.services.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.lordrose.vrms.converters.ContractConverter.toContractResponse;
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
    private final ProviderRepository providerRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public List<ContractResponse> findAll() {
        return toContractResponses(contractRepository.findAll());
    }

    @Override
    public ContractResponse registerProvider(ContactRequest contactRequest,
                                             MultipartFile[] images) {
        Contract saved = contractRepository.save(Contract.builder()
                .fullName(contactRequest.getFullName())
                .address(contactRequest.getAddress())
                .phoneNumber(contactRequest.getPhoneNumber())
                .email(contactRequest.getEmail())
                .status(ContractStatus.PENDING)
                .proofImageUrls(storageService.uploadFiles(images))
                .build());

        return toContractResponse(saved);
    }

    @Override
    public Object confirmContract(Long contractId, MultipartFile[] contractImages) {
        Contract result = contractRepository.findById(contractId)
                .orElseThrow(() -> newExceptionWithId(contractId));

        result.setContractFileUrls(storageService.uploadFiles(contractImages));
        result.setStatus(ContractStatus.CONFIRMED);

        return toContractResponse(contractRepository.save(result));
    }

    @Override
    public Object resolvedContract(Long contractId, ProviderRequest providerRequest,
                                   ManagerCreateRequest managerRequest, MultipartFile[] images) {
        userRepository.findUserByUsername(managerRequest.getUsername())
                .ifPresent(user -> {
                    throw new InvalidArgumentException("User " + user.getUsername() +
                            " is already existed!");
                });

        Contract result = contractRepository.findById(contractId)
                .orElseThrow(() -> newExceptionWithId(contractId));
        result.setStatus(ContractStatus.RESOLVED);

        Contract saved = contractRepository.save(result);

        Provider created = providerRepository.save(Provider.builder()
                .name(providerRequest.getProviderName())
                .address(providerRequest.getAddress())
                .latitude(providerRequest.getLatitude())
                .longitude(providerRequest.getLongitude())
                .openTime(toLocalTime(providerRequest.getOpenTime()))
                .closeTime(toLocalTime(providerRequest.getCloseTime()))
                .slotCapacity(providerRequest.getSlotCapacity())
                .slotDuration(providerRequest.getSlotDuration())
                .imageUrls(storageService.uploadFiles(images))
                .isActive(false)
                .imageUrls(storageService.uploadFiles(images))
                .contract(saved)
                .build());

        final String password = getRandomAlphabeticWithLength(8);

        User manager = userRepository.save(User.builder()
                .username(managerRequest.getUsername())
                .password(password)
                .fullName(managerRequest.getFullName())
                .gender(managerRequest.getGender())
                .imageUrl("default")
                .isActive(true)
                .role(roleRepository.findByNameIgnoreCase("MANAGER")
                        .orElseThrow(() -> newExceptionWithValue("MANAGER")))
                .provider(created)
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

        return toProviderDetailResponse(created);
    }

    @Override
    public Object denyContract(Long contractId) {
        Contract result = contractRepository.findById(contractId)
                .orElseThrow(() -> newExceptionWithId(contractId));

        result.setStatus(ContractStatus.DENIED);

        return toContractResponse(contractRepository.save(result));
    }
}
