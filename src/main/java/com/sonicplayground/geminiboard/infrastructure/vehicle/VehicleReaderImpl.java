package com.sonicplayground.geminiboard.infrastructure.vehicle;

import com.sonicplayground.geminiboard.domain.vehicle.Vehicle;
import com.sonicplayground.geminiboard.domain.vehicle.VehicleReader;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VehicleReaderImpl implements VehicleReader {

    private final VehicleJpaRepository vehicleRepository;

    @Override
    public Optional<Vehicle> findByKey(UUID vehicleKey) {
        return vehicleRepository.findByKey(vehicleKey);
    }

    @Override
    public Optional<Vehicle> findByName(String vehicleName) {
        return vehicleRepository.findByName(vehicleName);
    }
}
