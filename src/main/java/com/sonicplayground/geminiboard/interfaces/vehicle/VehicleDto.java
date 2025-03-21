package com.sonicplayground.geminiboard.interfaces.vehicle;

import com.sonicplayground.geminiboard.domain.vehicle.VehicleCommand;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class VehicleDto {

    @AllArgsConstructor
    public static class CreateVehicleResponse {

        public UUID vehicleKey;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateVehicleRequest {

        @NotBlank(message = "require param : name")
        private String name;
        private String vehiclePicture;
        private String vin;
        @NotBlank(message = "require param : manufacturer")
        private String manufacturer;
        private Map<String, String> status;
        @NotBlank(message = "require param : modelName")
        private String modelName;
        @Min(1800)
        private Integer purchaseYear;
        private String registrationPicture;
        private String memo;

        public VehicleCommand.CreateVehicleRequest toCommand(String userKeyStr) {
            return VehicleCommand.CreateVehicleRequest.builder()
                .name(name)
                .vehiclePicture(vehiclePicture)
                .vin(vin)
                .manufacturer(manufacturer)
                .status(status)
                .modelName(modelName)
                .purchaseYear(purchaseYear)
                .registrationPicture(registrationPicture)
                .memo(memo)
                .ownerUserKey(UUID.fromString(userKeyStr))
                .build();
        }
    }
}
