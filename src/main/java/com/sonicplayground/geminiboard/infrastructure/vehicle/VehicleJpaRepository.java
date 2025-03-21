package com.sonicplayground.geminiboard.infrastructure.vehicle;

import com.sonicplayground.geminiboard.domain.vehicle.Vehicle;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleJpaRepository extends JpaRepository<Vehicle, Long> {


    Optional<Vehicle> findByKey(UUID vehicleKey);

    Optional<Vehicle> findByName(String vehicleName);
}
