package com.sonicplayground.geminiboard.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
@SQLRestriction("is_valid = true")
@SQLDelete(sql = "UPDATE users SET is_valid = false WHERE user_seq = ?")
public class User {

    @Id
    @Column(name = "user_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(nullable = false, unique = true, name = "user_key")
    private UUID key; // GUID

    @Column(name = "user_nm")
    private String name; // 이름

    private String nickname; // 닉네임

    @Enumerated(EnumType.STRING)
    private Gender gender; // 성별

    private Integer age; // 나이 todo birth

    @Column(name = "user_addr")
    private String address; // 주소

    @Enumerated(EnumType.STRING)
    private UserType userType; // 사용자 타입 (서비스관리자, 서비스이용자, 정비소 관리자)

    private String profilePicture; // 프로필 사진 URL

    private String loginId; // 로그인용 ID

    private String password; // 로그인용 PW

    @Builder.Default
    @ColumnDefault("true")
    private boolean isValid = true;

    @PrePersist
    protected void onCreate() {
        if (this.key == null) {
            this.key = UUID.randomUUID();
        }
    }

    public void update(String nickname, String address, String profilePicture) {
        if (StringUtils.hasText(nickname)) {
            this.nickname = nickname;
        }
        if (StringUtils.hasText(address)) {
            this.address = address;
        }
        if (StringUtils.hasText(profilePicture)) {
            this.profilePicture = profilePicture;
        }
    }

    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }
}