package com.sonicplayground.geminiboard.domain.vehicle;

import com.sonicplayground.geminiboard.domain.user.User;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

public class VehicleCommand {

    @Getter
    @Builder
    public static class CreateVehicleRequest {

        private final String name;
        private final String vehiclePicture;
        private final String vin;
        private final String manufacturer;
        private final Map<String, String> status;
        private final String modelName;
        private final int purchaseYear;
        private final String registrationPicture;
        private final String memo;
        private final UUID ownerUserKey;

        public Vehicle toEntity(User user) {
            return Vehicle.builder()
                .name(name)
                .vehiclePicture(vehiclePicture)
                .vin(vin)
                .manufacturer(manufacturer)
                .status(status)
                .modelName(modelName)
                .purchaseYear(purchaseYear)
                .registrationPicture(registrationPicture)
                .memo(memo)
                .owner(user)
                .build();
        }
    }

    @Getter
    @Builder
    public static class UpdateVehicleRequest {
        private final String name;
        private final String vehiclePicture;
        private final String modelName;
        private final String registrationPicture;
        private final String memo;
    }
}
