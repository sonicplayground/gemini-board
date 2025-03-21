package com.sonicplayground.geminiboard.interfaces.vehicle;


import com.sonicplayground.geminiboard.application.vehicle.VehicleApplicationService;
import com.sonicplayground.geminiboard.interfaces.vehicle.VehicleDto.CreateVehicleResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/vehicles")
public class VehicleController {

    private final VehicleApplicationService vehicleApplicationService;

    @PostMapping
    @PreAuthorize("hasAuthority('SERVICE_USER')")
    public ResponseEntity<CreateVehicleResponse> createVehicle(
        @AuthenticationPrincipal User requester,
        @RequestBody @Valid VehicleDto.CreateVehicleRequest request) {
        UUID vehicleToken = vehicleApplicationService.createVehicle(
            request.toCommand(requester.getUsername()));
        var response = new CreateVehicleResponse(vehicleToken);
        return ResponseEntity.ok(response);
    }
}
