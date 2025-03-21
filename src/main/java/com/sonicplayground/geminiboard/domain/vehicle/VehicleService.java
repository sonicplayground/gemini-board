package com.sonicplayground.geminiboard.domain.vehicle;

import com.sonicplayground.geminiboard.domain.user.User;
import com.sonicplayground.geminiboard.domain.vehicle.VehicleCommand.CreateVehicleRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 차량 비즈니스 로직
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleReader vehicleReader;
    private final VehicleStore vehicleStore;


    @Transactional
    public Vehicle createVehicle(User user, CreateVehicleRequest command) {
        // vehicle name 중복 체크
        vehicleReader.findByName(command.getName()).ifPresent(vehicle -> {
            throw new IllegalArgumentException("이미 존재하는 차량 이름 입니다.");
        });

        // 차량 생성
        Vehicle newVehicle = command.toEntity(user);
        return vehicleStore.save(newVehicle);
    }
}
