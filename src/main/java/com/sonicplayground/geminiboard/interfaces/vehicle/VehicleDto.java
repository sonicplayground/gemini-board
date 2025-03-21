package com.sonicplayground.geminiboard.interfaces.vehicle;

import com.sonicplayground.geminiboard.domain.user.User;
import com.sonicplayground.geminiboard.domain.vehicle.Vehicle;
import com.sonicplayground.geminiboard.domain.vehicle.VehicleCommand;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class VehicleDto {

    @AllArgsConstructor
    public static class CreateVehicleResponse {

        public UUID vehicleKey;
    }

    @AllArgsConstructor
    public static class ResultMessageResponse {

        public String result;
    }

    @AllArgsConstructor
    @Getter
    public static class MaintenanceRequest {

        @NotBlank(message = "require param : maintenanceType")
        private String maintenanceType;
        private LocalDate changeDate;
    }

    @AllArgsConstructor
    @Getter
    public static class UpdateMileageRequest {

        @PositiveOrZero(message = "mileage must be positive")
        private int mileage;
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

    @Getter
    @ToString
    public static class VehicleResponse {

        private final UUID vehicleKey;
        private final String name;
        private final String vehiclePicture;
        private final String vin;
        private final String manufacturer;
        private final Map<String, String> status;
        private final String modelName;
        private final Integer purchaseYear;
        private final String registrationPicture;
        private final String memo;
        private final UUID ownerUserKey;

        public VehicleResponse(Vehicle vehicle) {
            this.vehicleKey = vehicle.getKey();
            this.name = vehicle.getName();
            this.vehiclePicture = vehicle.getVehiclePicture();
            this.vin = vehicle.getVin();
            this.manufacturer = vehicle.getManufacturer();
            this.status = vehicle.getStatus();
            this.modelName = vehicle.getModelName();
            this.purchaseYear = vehicle.getPurchaseYear();
            this.registrationPicture = vehicle.getRegistrationPicture();
            this.memo = vehicle.getMemo();
            this.ownerUserKey = vehicle.getOwner().getKey();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VehicleSearchCondition {

        private int page;
        private int size;
        private String vin;
        private String ownerUserKey;
        private Long ownerUserSeq;
        private String manufacturer;
        private String modelName;
        private String vehicleKey;
        private Integer minMileage;
        private Integer maxMileage;

        public void setOwnerUserSeq(User user) {
            if (user == null) {
                this.ownerUserSeq = null;
            } else {
                this.ownerUserSeq = user.getSeq();
            }
            this.ownerUserKey = null;
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateVehicleRequest {

        private String name;
        private String vehiclePicture;
        private String modelName;
        private String registrationPicture;
        private String memo;

        public VehicleCommand.UpdateVehicleRequest toCommand() {
            return VehicleCommand.UpdateVehicleRequest.builder()
                .name(name)
                .vehiclePicture(vehiclePicture)
                .modelName(modelName)
                .registrationPicture(registrationPicture)
                .memo(memo)
                .build();
        }
    }
}
