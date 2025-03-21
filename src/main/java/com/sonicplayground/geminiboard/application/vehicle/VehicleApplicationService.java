package com.sonicplayground.geminiboard.application.vehicle;

import com.sonicplayground.geminiboard.domain.user.User;
import com.sonicplayground.geminiboard.domain.user.UserService;
import com.sonicplayground.geminiboard.domain.vehicle.Vehicle;
import com.sonicplayground.geminiboard.domain.vehicle.VehicleCommand.CreateVehicleRequest;
import com.sonicplayground.geminiboard.domain.vehicle.VehicleService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleApplicationService {

    private final VehicleService vehicleService;
    private final UserService userService;


    public UUID createVehicle(CreateVehicleRequest command) {
        User user = userService.getUserByUserKey(command.getOwnerUserKey());
        Vehicle created = vehicleService.createVehicle(user, command);
        return created.getKey();
    }
}
