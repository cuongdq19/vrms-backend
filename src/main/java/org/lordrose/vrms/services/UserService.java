package org.lordrose.vrms.services;

import org.lordrose.vrms.models.requests.EmployeeRequest;
import org.lordrose.vrms.models.requests.LoginRequest;
import org.lordrose.vrms.models.requests.UserInfoRequest;
import org.lordrose.vrms.models.requests.UserSignUpRequest;
import org.lordrose.vrms.models.responses.UserInfoResponse;
import org.lordrose.vrms.models.responses.UserVehicleInfoResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    void verifyPhoneNumber(String phoneNumber);

    UserVehicleInfoResponse findByPhoneNumber(String phoneNumber);

    List<UserInfoResponse> findAllByProviderId(Long providerId);

    UserInfoResponse createEmployee(Long providerId, EmployeeRequest request, MultipartFile image);

    UserInfoResponse updateEmployee(Long userId, EmployeeRequest request, MultipartFile image);

    UserInfoResponse loginWithPhoneNumber(LoginRequest request);

    UserInfoResponse loginWithUsername(LoginRequest request);

    UserInfoResponse toggleUserStatus(Long userId);

    UserInfoResponse updateUserInfo(Long id, UserInfoRequest request);

    UserInfoResponse registerUser(UserSignUpRequest request);

    UserInfoResponse deleteEmployee(Long userId);
}
