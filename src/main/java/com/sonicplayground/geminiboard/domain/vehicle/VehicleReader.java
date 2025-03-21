package com.sonicplayground.geminiboard.domain.vehicle;

import java.util.Optional;
import java.util.UUID;

/**
 * 차량 저장소 인터페이스
 */
public interface VehicleReader {

    Optional<Vehicle> findByKey(UUID vehicleKey);
    Optional<Vehicle> findByName(String vehicleName);
}