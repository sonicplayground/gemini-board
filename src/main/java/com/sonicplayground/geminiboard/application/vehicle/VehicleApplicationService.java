package com.sonicplayground.geminiboard.application.vehicle;

import com.sonicplayground.geminiboard.domain.user.AuthService;
import com.sonicplayground.geminiboard.domain.user.User;
import com.sonicplayground.geminiboard.domain.user.UserService;
import com.sonicplayground.geminiboard.domain.vehicle.Vehicle;
import com.sonicplayground.geminiboard.domain.vehicle.VehicleCommand.CreateVehicleRequest;
import com.sonicplayground.geminiboard.domain.vehicle.VehicleCommand.UpdateVehicleRequest;
import com.sonicplayground.geminiboard.domain.vehicle.VehicleService;
import com.sonicplayground.geminiboard.interfaces.user.LoginDto;
import com.sonicplayground.geminiboard.interfaces.user.LoginDto.RequesterInfo;
import com.sonicplayground.geminiboard.interfaces.vehicle.VehicleDto.VehicleResponse;
import com.sonicplayground.geminiboard.interfaces.vehicle.VehicleDto.VehicleSearchCondition;
import java.time.LocalDate;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleApplicationService {

    private final VehicleService vehicleService;
    private final UserService userService;
    private final AuthService authService;


    public UUID createVehicle(CreateVehicleRequest command) {
        User user = userService.getUserByUserKey(command.getOwnerUserKey());
        Vehicle created = vehicleService.createVehicle(user, command);
        return created.getKey();
    }

    public VehicleResponse getVehicle(LoginDto.RequesterInfo requesterInfo, UUID vehicleKey) {
        Vehicle vehicle = vehicleService.getVehicle(vehicleKey);
        authService.checkAuthority(requesterInfo, vehicle.getOwner().getKey());
        return new VehicleResponse(vehicle);
    }

    public Page<VehicleResponse> getVehicles(RequesterInfo requesterInfo,
        VehicleSearchCondition condition, Pageable pageable) {

        UUID targetUserKey = vehicleService.getUserKeyByPolicy(requesterInfo, condition);
        User targetUser =
            targetUserKey == null ? null : userService.getUserByUserKey(targetUserKey);
        condition.setOwnerUserSeq(targetUser);

        Page<Vehicle> vehicles = vehicleService.getVehicles(condition, pageable);

        return vehicles.map(VehicleResponse::new);
    }

    public VehicleResponse updateVehicle(RequesterInfo requesterInfo, UUID vehicleKey,
        UpdateVehicleRequest command) {
        Vehicle vehicle = vehicleService.getVehicle(vehicleKey);
        authService.checkAuthority(requesterInfo, vehicle.getOwner().getKey());
        Vehicle updated = vehicleService.updateVehicle(vehicle, command);
        return new VehicleResponse(updated);
    }

    public void replaceEquipment(RequesterInfo requesterInfo, UUID vehicleKey,
        String maintenanceType, LocalDate changeDate) {

        Vehicle vehicle = vehicleService.getVehicle(vehicleKey);
        authService.checkAuthority(requesterInfo, vehicle.getOwner().getKey());
        vehicleService.replaceEquipment(vehicle, maintenanceType, changeDate);
    }

    public void updateMileage(RequesterInfo requesterInfo, UUID vehicleKey, int mileage) {
        Vehicle vehicle = vehicleService.getVehicle(vehicleKey);
        authService.checkAuthority(requesterInfo, vehicle.getOwner().getKey());
        vehicleService.updateMileage(vehicle, mileage);
    }
}
