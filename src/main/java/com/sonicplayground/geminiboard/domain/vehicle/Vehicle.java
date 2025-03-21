package com.sonicplayground.geminiboard.domain.vehicle;

import static com.sonicplayground.geminiboard.domain.common.Constant.VEHICLE_BRAKE_PAD_REPLACEMENT_DATE;
import static com.sonicplayground.geminiboard.domain.common.Constant.VEHICLE_ENGINE_OIL_CHANGE_DATE;
import static com.sonicplayground.geminiboard.domain.common.Constant.VEHICLE_MILEAGE;
import static com.sonicplayground.geminiboard.domain.common.Constant.VEHICLE_TIRE_REPLACEMENT_DATE;

import com.sonicplayground.geminiboard.domain.common.BaseEntity;
import com.sonicplayground.geminiboard.domain.user.User;
import com.sonicplayground.geminiboard.domain.vehicle.VehicleCommand.UpdateVehicleRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;
import org.springframework.util.StringUtils;

/**
 * 차량 엔티티
 */
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "vehicles")
@SQLRestriction("is_valid = true")
@SQLDelete(sql = "UPDATE vehicles SET is_valid = false WHERE vehicle_seq = ?")
public class Vehicle extends BaseEntity {

    @Id
    @Column(name = "vehicle_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(nullable = false, unique = true, name = "vehicle_key")
    private UUID key; // GUID

    @Column(nullable = false, name = "vehicle_nm", length = 25)
    private String name; // 닉네임

    private String vehiclePicture; // 차량 사진 URL

    private String vin; // 차대번호

    @Column(nullable = false)
    private String manufacturer; // 제조사

    @Column(name = "vehicle_stts", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, String> status; // 차량 상태

    @Column(nullable = false, name = "model_nm")
    private String modelName; // 모델명

    @Column(name = "purchase_year", nullable = false)
    private int purchaseYear; // 차량 연식

    private String registrationPicture; // 차량 등록증 사진 URL

    @Column(columnDefinition = "TEXT")
    private String memo; // 메모

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private User owner; // 소유자

    @Builder.Default
    @ColumnDefault("true")
    private boolean isValid = true;

    @PrePersist
    protected void onCreate() {
        if (this.key == null) {
            this.key = UUID.randomUUID();
        }

        if (this.status == null) {
            String cand = String.format("%d-01-01", this.purchaseYear);
            this.status = Map.of(
                VEHICLE_MILEAGE, String.valueOf((LocalDate.now().getYear() - this.purchaseYear) * 10000),
                VEHICLE_TIRE_REPLACEMENT_DATE, cand,
                VEHICLE_ENGINE_OIL_CHANGE_DATE, cand,
                VEHICLE_BRAKE_PAD_REPLACEMENT_DATE, cand
            );
        }
    }

    public void updateMileage(int mileage) {
        this.status.put(VEHICLE_MILEAGE, String.valueOf(mileage));
    }

    public void replaceTireOn(LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        this.status.put(VEHICLE_TIRE_REPLACEMENT_DATE, String.valueOf(date));
    }

    public void changeEngineOilOn(LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        this.status.put(VEHICLE_ENGINE_OIL_CHANGE_DATE, String.valueOf(date));
    }

    public void replaceBreakPadOn(LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        this.status.put(VEHICLE_BRAKE_PAD_REPLACEMENT_DATE, String.valueOf(date));
    }

    public void updateBasicInfos(UpdateVehicleRequest command) {
        if (StringUtils.hasText(command.getName())){
            this.name = command.getName();
        }
        if (StringUtils.hasText(command.getVehiclePicture())) {
            this.vehiclePicture = command.getVehiclePicture();
        }
        if (StringUtils.hasText(command.getModelName())) {
            this.modelName = command.getModelName();
        }
        if (StringUtils.hasText(command.getRegistrationPicture())) {
            this.registrationPicture = command.getRegistrationPicture();
        }
        if (StringUtils.hasText(command.getMemo())) {
            this.memo = command.getMemo();
        }
    }
}
