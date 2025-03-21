package com.sonicplayground.geminiboard.domain.vehicle;

import com.sonicplayground.geminiboard.domain.common.Constant;
import com.sonicplayground.geminiboard.domain.user.User;
import com.sonicplayground.geminiboard.domain.user.UserType;
import com.sonicplayground.geminiboard.domain.vehicle.VehicleCommand.CreateVehicleRequest;
import com.sonicplayground.geminiboard.domain.vehicle.VehicleCommand.UpdateVehicleRequest;
import com.sonicplayground.geminiboard.interfaces.user.LoginDto.RequesterInfo;
import com.sonicplayground.geminiboard.interfaces.vehicle.VehicleDto.VehicleSearchCondition;
import java.time.LocalDate;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Vehicle getVehicle(UUID vehicleKey) {
        return vehicleReader.findByKey(vehicleKey)
            .orElseThrow(() -> new IllegalArgumentException("차량이 존재하지 않습니다."));
    }

    public Page<Vehicle> getVehicles(VehicleSearchCondition condition, Pageable pageable) {
        return vehicleReader.findAll(condition, pageable);
    }

    public UUID getUserKeyByPolicy(RequesterInfo requesterInfo, VehicleSearchCondition condition) {
        UUID targetUserKey;
        if (UserType.SERVICE_USER.equals(requesterInfo.getUserType())) {
            targetUserKey = requesterInfo.getUserKey();
        } else {
            targetUserKey = UUID.fromString(condition.getOwnerUserKey());
        }
        return targetUserKey;
    }

    @Transactional
    public Vehicle updateVehicle(Vehicle vehicle, UpdateVehicleRequest command) {
        vehicle.updateBasicInfos(command);
        return vehicle;
    }

    @Transactional
    public void replaceEquipment(Vehicle vehicle, String maintenanceType, LocalDate changeDate) {
        if (changeDate == null) {
            changeDate = LocalDate.now();
        }

        if (Constant.TIRE.equals(maintenanceType)) {
            vehicle.replaceTireOn(changeDate);
        } else if (Constant.ENGINE_OIL.equals(maintenanceType)) {
            vehicle.changeEngineOilOn(changeDate);
        } else if (Constant.BRAKE_PAD.equals(maintenanceType)) {
            vehicle.replaceBreakPadOn(changeDate);
        } else {
            throw new IllegalArgumentException("유효 하지 않은 유지 보수 유형 입니다.");
        }
    }

    public void updateMileage(Vehicle vehicle, int mileage) {
        vehicle.updateMileage(mileage);
    }
}
