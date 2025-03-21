package com.sonicplayground.geminiboard.domain.vehicle;

/**
 * 차량 저장소 인터페이스
 */
public interface VehicleStore {

    Vehicle save(Vehicle newUser);

    void delete(Vehicle user);
}