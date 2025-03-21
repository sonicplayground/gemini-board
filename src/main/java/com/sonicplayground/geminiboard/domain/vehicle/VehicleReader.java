package com.sonicplayground.geminiboard.domain.vehicle;

import com.sonicplayground.geminiboard.interfaces.vehicle.VehicleDto.VehicleSearchCondition;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 차량 저장소 인터페이스
 */
public interface VehicleReader {

    Optional<Vehicle> findByKey(UUID vehicleKey);
    Optional<Vehicle> findByName(String vehicleName);

    Page<Vehicle> findAll(VehicleSearchCondition condition, Pageable pageable);
}