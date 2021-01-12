package org.lordrose.vrms.controllers;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.models.requests.EmployeeRequest;
import org.lordrose.vrms.models.requests.LoginRequest;
import org.lordrose.vrms.models.requests.UserInfoRequest;
import org.lordrose.vrms.models.requests.UserSignUpRequest;
import org.lordrose.vrms.models.responses.UserInfoResponse;
import org.lordrose.vrms.models.responses.UserVehicleInfoResponse;
import org.lordrose.vrms.services.UserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/verify/{phoneNumber}")
    public void verifyPhoneNumberStatus(@PathVariable String phoneNumber) {
        userService.verifyPhoneNumber(phoneNumber);
    }

    @GetMapping("/phone/{phoneNumber}")
    public UserVehicleInfoResponse findUserByPhoneNumber(@PathVariable String phoneNumber) {
        return userService.findByPhoneNumber(phoneNumber);
    }

    @GetMapping("/provider/{providerId}")
    public List<UserInfoResponse> findUsersByProviderId(@PathVariable Long providerId) {
        return userService.findAllByProviderId(providerId);
    }

    @PostMapping("/provider/{providerId}")
    public UserInfoResponse createUserByProvider(@PathVariable Long providerId,
                                                 @ModelAttribute EmployeeRequest request,
                                                 @RequestPart MultipartFile image) {
        return userService.createEmployee(providerId, request, image);
    }

    @PostMapping("/{userId}/provider")
    public UserInfoResponse updateEmployeeByProvider(@PathVariable Long userId,
                                                 @ModelAttribute EmployeeRequest request,
                                                 @RequestPart MultipartFile image) {
        return userService.updateEmployee(userId, request, image);
    }

    @GetMapping("/{userId}/provider")
    public UserInfoResponse deleteEmployeeByProvider(@PathVariable Long userId) {
        return userService.deleteEmployee(userId);
    }

    @PostMapping
    public UserInfoResponse loginWithPhoneNumber(@RequestBody LoginRequest request) {
        return userService.loginWithPhoneNumber(request);
    }

    @PostMapping("/provider")
    public UserInfoResponse loginWithUsername(@RequestBody LoginRequest request) {
        return userService.loginWithUsername(request);
    }

    @DeleteMapping("/user/{userId}")
    public UserInfoResponse toggleUserStatus(@PathVariable Long userId) {
        return userService.toggleUserStatus(userId);
    }

    @PostMapping("/{id}")
    public UserInfoResponse editUserInfo(@PathVariable Long id,
                                         @RequestBody UserInfoRequest request) {
        return userService.updateUserInfo(id, request);
    }

    @PostMapping("/signup")
    public UserInfoResponse processUserSignup(@RequestBody UserSignUpRequest request) {
        return userService.registerUser(request);
    }
}
