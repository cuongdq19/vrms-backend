package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.Request;
import org.lordrose.vrms.domains.User;
import org.lordrose.vrms.domains.Vehicle;
import org.lordrose.vrms.models.responses.TechnicianResponse;
import org.lordrose.vrms.models.responses.UserInfoResponse;
import org.lordrose.vrms.models.responses.UserRequestInfoResponse;
import org.lordrose.vrms.models.responses.UserVehicleInfoResponse;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.lordrose.vrms.converters.VehicleConverter.toVehicleRequestInfoResponses;

public class UserConverter {

    public static UserVehicleInfoResponse toUserVehicleInfoResponse(User user,
                                                                    Map<Vehicle, Optional<Request>> resultMap) {
        return UserVehicleInfoResponse.builder()
                .userId(user.getId())
                .phoneNumber(user.getPhoneNumber())
                .fullName(user.getFullName())
                .gender(user.getGender())
                .vehicles(toVehicleRequestInfoResponses(resultMap))
                .build();
    }

    public static UserInfoResponse toUserInfoResponse(User user) {
        return UserInfoResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .gender(user.getGender())
                .imgUrl(user.getImageUrl())
                .roleId(user.getRole().getId())
                .roleName(user.getRole().getName())
                .isActive(user.getIsActive())
                .providerId(user.getProvider().getId())
                .build();
    }

    public static List<UserInfoResponse> toUserInfoResponses(Collection<User> users) {
        return users.stream()
                .map(UserConverter::toUserInfoResponse)
                .collect(Collectors.toList());
    }

    public static UserRequestInfoResponse toRequestUserInfoResponse(User user) {
        return UserRequestInfoResponse.builder()
                .id(user.getId())
                .phoneNumber(user.getPhoneNumber())
                .fullName(user.getFullName())
                .gender(user.getGender())
                .imageUrl(user.getImageUrl())
                .build();
    }

    public static TechnicianResponse toTechnicianResponse(User user) {
        return TechnicianResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .gender(user.getGender())
                .imageUrl(user.getImageUrl())
                .build();
    }
}
