package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.Provider;
import org.lordrose.vrms.domains.Request;
import org.lordrose.vrms.domains.Role;
import org.lordrose.vrms.domains.User;
import org.lordrose.vrms.domains.Vehicle;
import org.lordrose.vrms.exceptions.InvalidArgumentException;
import org.lordrose.vrms.exceptions.ResourceNotFoundException;
import org.lordrose.vrms.models.requests.EmployeeRequest;
import org.lordrose.vrms.models.requests.LoginRequest;
import org.lordrose.vrms.models.requests.UserInfoRequest;
import org.lordrose.vrms.models.requests.UserSignUpRequest;
import org.lordrose.vrms.models.responses.UserInfoResponse;
import org.lordrose.vrms.models.responses.UserVehicleInfoResponse;
import org.lordrose.vrms.repositories.ProviderRepository;
import org.lordrose.vrms.repositories.RequestRepository;
import org.lordrose.vrms.repositories.RoleRepository;
import org.lordrose.vrms.repositories.UserRepository;
import org.lordrose.vrms.repositories.VehicleRepository;
import org.lordrose.vrms.services.StorageService;
import org.lordrose.vrms.services.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.lordrose.vrms.converters.UserConverter.toUserInfoResponse;
import static org.lordrose.vrms.converters.UserConverter.toUserInfoResponses;
import static org.lordrose.vrms.converters.UserConverter.toUserVehicleInfoResponse;
import static org.lordrose.vrms.exceptions.ResourceNotFoundException.newExceptionWithId;
import static org.lordrose.vrms.exceptions.ResourceNotFoundException.newExceptionWithValue;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProviderRepository providerRepository;
    private final RoleRepository roleRepository;
    private final VehicleRepository vehicleRepository;
    private final RequestRepository requestRepository;

    private final StorageService storageService;
    private final FirebaseNotificationServiceImpl notificationService;

    @Override
    public void verifyPhoneNumber(String phoneNumber) {
        userRepository.findUserByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("User with phone number: " +
                        phoneNumber + " is not found!"));
    }

    @Override
    public UserVehicleInfoResponse findByPhoneNumber(String phoneNumber) {
        User result = userRepository.findUserByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("User with phone number: " +
                        phoneNumber + " is not found!"));

        Map<Vehicle, Optional<Request>> resultMap = vehicleRepository.findAllByUserId(result.getId()).stream()
                .collect(Collectors.toMap(
                        vehicle -> vehicle,
                        vehicle -> requestRepository
                                .findFirstByVehicleIdAndCheckoutTimeNotNullOrderByCheckoutTimeDesc(vehicle.getId())));
        return toUserVehicleInfoResponse(result, resultMap);
    }

    @Override
    public List<UserInfoResponse> findAllByProviderId(Long providerId) {
        return toUserInfoResponses(userRepository.findAllByProviderId(providerId));
    }

    @Override
    public UserInfoResponse createEmployee(Long providerId, EmployeeRequest request,
                                           MultipartFile image) {
        String imageUrl = "";
        if (image != null) {
            imageUrl = storageService.uploadFile(image);
        }
        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> newExceptionWithId(providerId));
        Role role;
        User saved;
        if ("TECHNICIAN".equalsIgnoreCase(request.getRoleName())) {
            role = roleRepository.findByNameIgnoreCase("TECHNICIAN")
                    .orElseThrow(() -> newExceptionWithValue("TECHNICIAN"));
            saved = userRepository.save(User.builder()
                    .fullName(request.getFullName())
                    .gender(request.getGender())
                    .isActive(true)
                    .imageUrl(imageUrl)
                    .role(role)
                    .provider(provider)
                    .build());
        } else {
            role = roleRepository.findByNameIgnoreCase("STAFF")
                    .orElseThrow(() -> newExceptionWithValue("STAFF"));
            saved = userRepository.save(User.builder()
                    .username(request.getUsername())
                    .password(request.getPassword())
                    .fullName(request.getFullName())
                    .gender(request.getGender())
                    .imageUrl(imageUrl)
                    .isActive(true)
                    .role(role)
                    .provider(provider)
                    .build());
        }
        return toUserInfoResponse(saved);
    }

    @Override
    public UserInfoResponse updateEmployee(Long userId, EmployeeRequest request) {
        User saved = userRepository.findById(userId)
                .orElseThrow(() -> newExceptionWithId(userId));

        if (!"TECHNICIAN".equalsIgnoreCase(request.getRoleName())) {
            saved.setPassword(request.getPassword());
        }
        saved.setFullName(request.getFullName());
        saved.setGender(request.getGender());

        return toUserInfoResponse(userRepository.save(saved));
    }

    @Override
    public UserInfoResponse loginWithPhoneNumber(LoginRequest request) {
        User result = userRepository.findUserByPhoneNumberAndPassword(
                request.getPhoneNumber(), request.getPassword())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        result.setDeviceToken(request.getDeviceToken());

        return toUserInfoResponse(userRepository.save(result));
    }

    @Override
    public UserInfoResponse loginWithUsername(LoginRequest request) {
        User result = userRepository.findUserByUsernameAndPassword(
                request.getUsername(), request.getPassword())
                .orElseThrow();
        if ("ADMIN".equalsIgnoreCase(result.getRole().getName())) {
            return toUserInfoResponse(result);
        }

        result.setDeviceToken(request.getDeviceToken());

        notificationService.subscribeProviderTopic(result.getDeviceToken(),
                result.getProvider().getId());

        return toUserInfoResponse(userRepository.save(result));
    }

    @Override
    public UserInfoResponse toggleUserStatus(Long userId) {
        User result = userRepository.findById(userId)
                .orElseThrow(() -> newExceptionWithId(userId));

        result.setIsActive(!result.getIsActive());

        return toUserInfoResponse(userRepository.save(result));
    }

    @Override
    public UserInfoResponse updateUserInfo(Long id, UserInfoRequest request) {
        User result = userRepository.findById(id)
                .orElseThrow(() -> newExceptionWithId(id));

        result.setFullName(request.getFullName());
        result.setGender(request.getGender());

        return toUserInfoResponse(userRepository.save(result));
    }

    @Override
    public UserInfoResponse registerUser(UserSignUpRequest request) {
        userRepository.findUserByPhoneNumber(request.getPhoneNumber())
                .ifPresent(user -> {
                    throw new InvalidArgumentException("PhoneNumber: " + user.getPhoneNumber() +
                            " is already existed.");
                });
        User saved = userRepository.save(User.builder()
                .phoneNumber(request.getPhoneNumber())
                .password(request.getPassword())
                .fullName(request.getFullName())
                .gender(request.getGender())
                .imageUrl("image url")
                .isActive(true)
                .deviceToken(request.getDeviceToken())
                .role(roleRepository.findByNameIgnoreCase("USER")
                        .orElseThrow(() -> newExceptionWithValue("USER")))
                .build());
        return toUserInfoResponse(saved);
    }

    @Override
    public UserInfoResponse toggleEmployeeStatus(Long userId) {
        User saved = userRepository.findById(userId)
                .orElseThrow(() -> newExceptionWithId(userId));

        saved.setIsActive(!saved.getIsActive());

        return toUserInfoResponse(userRepository.save(saved));
    }

//    private boolean isValid(UserSignUpRequest request) {
//        try {
//            FirebaseAuth auth = FirebaseAuth.getInstance();
//            UserRecord record = auth.getUserByPhoneNumber(request.getPhoneNumber());
//            return record.getUid().equals(request.getUid());
//        } catch (FirebaseAuthException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
}
