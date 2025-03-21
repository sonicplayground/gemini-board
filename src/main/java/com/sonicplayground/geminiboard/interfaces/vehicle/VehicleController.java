package com.sonicplayground.geminiboard.interfaces.vehicle;


import com.sonicplayground.geminiboard.application.vehicle.VehicleApplicationService;
import com.sonicplayground.geminiboard.common.response.PagedContent;
import com.sonicplayground.geminiboard.interfaces.user.LoginDto;
import com.sonicplayground.geminiboard.interfaces.user.LoginDto.RequesterInfo;
import com.sonicplayground.geminiboard.interfaces.vehicle.VehicleDto.CreateVehicleResponse;
import com.sonicplayground.geminiboard.interfaces.vehicle.VehicleDto.VehicleResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        UUID vehicleKey = vehicleApplicationService.createVehicle(
            request.toCommand(requester.getUsername()));
        var response = new CreateVehicleResponse(vehicleKey);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{vehicleKey}")
    public ResponseEntity<VehicleDto.VehicleResponse> getVehicle(
        @AuthenticationPrincipal User requester,
        @PathVariable UUID vehicleKey) {
        LoginDto.RequesterInfo requesterInfo = RequesterInfo.from(requester);
        VehicleDto.VehicleResponse vehicle = vehicleApplicationService.getVehicle(requesterInfo,
            vehicleKey);
        return ResponseEntity.ok(vehicle);

    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('SERVICE_ADMIN', 'SERVICE_USER')")
    public ResponseEntity<PagedContent<VehicleResponse>> getVehicle(
        @AuthenticationPrincipal User requester,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) String vin,
        @RequestParam(required = false) String ownerUserKey,
        @RequestParam(required = false) String manufacturer,
        @RequestParam(required = false) String modelName,
        @RequestParam(required = false) String vehicleKey,
        @RequestParam(required = false) Integer minMileage,
        @RequestParam(required = false) Integer maxMileage) {

        Pageable pageable = PageRequest.of(page, size);
        LoginDto.RequesterInfo requesterInfo = RequesterInfo.from(requester);

        VehicleDto.VehicleSearchCondition condition = VehicleDto.VehicleSearchCondition.builder()
            .vin(vin)
            .ownerUserKey(ownerUserKey)
            .manufacturer(manufacturer)
            .modelName(modelName)
            .vehicleKey(vehicleKey)
            .minMileage(minMileage)
            .maxMileage(maxMileage)
            .build();

        Page<VehicleDto.VehicleResponse> vehicles = vehicleApplicationService.getVehicles(
            requesterInfo, condition, pageable);

        PagedContent<VehicleDto.VehicleResponse> result = new PagedContent<>(vehicles);

        return ResponseEntity.ok(result);

    }
}
