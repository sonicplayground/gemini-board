package com.sonicplayground.geminiboard.infrastructure.vehicle;

import com.sonicplayground.geminiboard.domain.vehicle.Vehicle;
import com.sonicplayground.geminiboard.domain.vehicle.VehicleReader;
import com.sonicplayground.geminiboard.interfaces.vehicle.VehicleDto.VehicleSearchCondition;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    @Override
    public Page<Vehicle> findAll(VehicleSearchCondition condition, Pageable pageable) {
        Specification<Vehicle> spec = searchVehicle(condition.getVin(), condition.getOwnerUserSeq(),
            condition.getManufacturer(),
            condition.getModelName(), condition.getVehicleKey(), condition.getMinMileage(),
            condition.getMaxMileage());
        return vehicleRepository.findAll(spec, pageable);
    }

    private Specification<Vehicle> searchVehicle(String vin, Long ownerUserSeq,
        String manufacturer, String modelName, String vehicleKey, Integer minMileage,
        Integer maxMileage) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // VIN 검색 (EQUAL 연산)
            if (vin != null && !vin.isEmpty()) {
                predicates.add(cb.equal(root.get("vin"), vin));
            }

            // vehicleKey 검색 (EQUAL 연산)
            if (vehicleKey != null && !vehicleKey.isEmpty()) {
                predicates.add(cb.equal(root.get("key"), vehicleKey));
            }

            // ownerSeq 검색 (EQUAL 연산)
            if (ownerUserSeq != null) {
                predicates.add(cb.equal(root.get("owner").get("seq"), ownerUserSeq));
            }

            // manufacturer 검색 (EQUAL 연산)
            if (manufacturer != null && !manufacturer.isEmpty()) {
                predicates.add(cb.equal(root.get("manufacturer"), manufacturer));
            }
            // modelName 검색 (LIKE 연산)
            if (modelName != null && !modelName.isEmpty()) {
                predicates.add(cb.like(root.get("modelName"), "%" + modelName + "%"));
            }

            // mileage 범위 검색 (BETWEEN 연산)
            if (minMileage != null && maxMileage != null) {
                predicates.add(cb.between(
                    cb.function("JSON_EXTRACT", Integer.class, root.get("status"),
                        cb.literal("$.mileage")),
                    minMileage, maxMileage));
            } else if (minMileage != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                    cb.function("JSON_EXTRACT", Integer.class, root.get("status"),
                        cb.literal("$.mileage")),
                    minMileage));
            } else if (maxMileage != null) {
                predicates.add(cb.lessThanOrEqualTo(
                    cb.function("JSON_EXTRACT", Integer.class, root.get("status"),
                        cb.literal("$.mileage")),
                    maxMileage));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
