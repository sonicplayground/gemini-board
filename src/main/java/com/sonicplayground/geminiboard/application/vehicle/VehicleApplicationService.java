package com.sonicplayground.geminiboard.application.vehicle;

import com.sonicplayground.geminiboard.domain.user.AuthService;
import com.sonicplayground.geminiboard.domain.user.User;
import com.sonicplayground.geminiboard.domain.user.UserService;
import com.sonicplayground.geminiboard.domain.vehicle.Vehicle;
import com.sonicplayground.geminiboard.domain.vehicle.VehicleCommand.CreateVehicleRequest;
import com.sonicplayground.geminiboard.domain.vehicle.VehicleService;
import com.sonicplayground.geminiboard.interfaces.user.LoginDto;
import com.sonicplayground.geminiboard.interfaces.user.LoginDto.RequesterInfo;
import com.sonicplayground.geminiboard.interfaces.vehicle.VehicleDto.VehicleResponse;
import com.sonicplayground.geminiboard.interfaces.vehicle.VehicleDto.VehicleSearchCondition;
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
        User targetUser = targetUserKey == null ? null : userService.getUserByUserKey(targetUserKey);
        condition.setOwnerUserSeq(targetUser);

        Page<Vehicle> vehicles = vehicleService.getVehicles(condition, pageable);

        return vehicles.map(VehicleResponse::new);
    }
}
