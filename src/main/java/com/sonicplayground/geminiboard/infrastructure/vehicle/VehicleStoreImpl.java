package com.sonicplayground.geminiboard.infrastructure.vehicle;

import com.sonicplayground.geminiboard.domain.vehicle.Vehicle;
import com.sonicplayground.geminiboard.domain.vehicle.VehicleStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VehicleStoreImpl implements VehicleStore {

    private final VehicleJpaRepository vehicleRepository;

    public Vehicle save(Vehicle newUser) {
        return vehicleRepository.save(newUser);
    }

    public void delete(Vehicle user) {
        vehicleRepository.delete(user);
    }


}
