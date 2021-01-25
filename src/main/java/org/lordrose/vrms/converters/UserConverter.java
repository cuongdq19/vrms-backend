package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.Request;
import org.lordrose.vrms.domains.User;
import org.lordrose.vrms.domains.Vehicle;
import org.lordrose.vrms.models.responses.TechnicianResponse;
import org.lordrose.vrms.models.responses.UserInfoResponse;
import org.lordrose.vrms.models.responses.UserRequestInfoResponse;
import org.lordrose.vrms.models.responses.UserVehicleHistoryResponse;
import org.lordrose.vrms.models.responses.UserVehicleInfoResponse;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.lordrose.vrms.converters.VehicleConverter.toVehicleRequestInfoResponses;
import static org.lordrose.vrms.converters.VehicleConverter.toVehicleResponse;
import static org.lordrose.vrms.utils.DateTimeUtils.toSeconds;
import static org.lordrose.vrms.utils.FileUrlUtils.getUrlsAsArray;

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
                .imgUrl(getUrlsAsArray(user.getImageUrl()))
                .roleId(user.getRole().getId())
                .roleName(user.getRole().getName())
                .isActive(user.getIsActive())
                .providerId(user.returnProviderId())
                .build();
    }

    public static List<UserInfoResponse> toUserInfoResponses(Collection<User> users) {
        return users.stream()
                .map(UserConverter::toUserInfoResponse)
                .collect(Collectors.toList());
    }

    public static UserRequestInfoResponse toRequestUserInfoResponse(Vehicle vehicle) {
        return UserRequestInfoResponse.builder()
                .id(vehicle.getUser().getId())
                .phoneNumber(vehicle.getUser().getPhoneNumber())
                .fullName(vehicle.getUser().getFullName())
                .gender(vehicle.getUser().getGender())
                .imageUrl(vehicle.getUser().getImageUrl())
                .vehicle(toVehicleResponse(vehicle))
                .build();
    }

    public static TechnicianResponse toTechnicianResponse(User user) {
        return TechnicianResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .gender(user.getGender())
                .imageUrl(getUrlsAsArray(user.getImageUrl()))
                .build();
    }

    public static TechnicianResponse toTechnicianHistoryResponse(User technician) {
        if (technician == null)
            return null;
        return TechnicianResponse.builder()
                .id(technician.getId())
                .fullName(technician.getFullName())
                .gender(technician.getGender())
                .imageUrl(getUrlsAsArray(technician.getImageUrl()))
                .build();
    }

    public static UserVehicleHistoryResponse toUserVehicleHistoryResponse(Vehicle vehicle) {
        return UserVehicleHistoryResponse.builder()
                .vehicleId(vehicle.getId())
                .plateNumber(vehicle.getPlateNumber())
                .vinNumber(vehicle.getVinNumber())
                .color(vehicle.getColor())
                .boughtDate(toSeconds(vehicle.getBoughtDate()))
                .modelId(vehicle.getModel().getId())
                .modelName(vehicle.getModel().getName())
                .modelYear(vehicle.getModel().getYear())
                .fuelType(vehicle.getModel().getFuelType())
                .gearbox(vehicle.getModel().getGearbox())
                .userId(vehicle.getUser().getId())
                .phoneNumber(vehicle.getUser().getPhoneNumber())
                .fullName(vehicle.getUser().getFullName())
                .gender(vehicle.getUser().getGender())
                .userImageUrl(vehicle.getUser().getImageUrl())
                .build();
    }
}
