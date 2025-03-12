package com.sonicplayground.geminiboard.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "user_uuid")
    private String uuid; // GUID

    @Column(name = "user_nm")
    private String name; // 이름

    private String nickname; // 닉네임

    private String gender; // 성별

    private Integer age; // 나이

    @Column(name = "user_addr")
    private String address; // 주소

    @Enumerated(EnumType.STRING)
    private UserType userType; // 사용자 타입 (서비스관리자, 서비스이용자, 정비소 관리자)

    private String profilePicture; // 프로필 사진 URL

    private String loginId; // 로그인용 ID

    private String password; // 로그인용 PW

    private boolean isValid;

    @Builder
    public User(String name, String nickname, String gender, Integer age, String address,
        UserType userType, String profilePicture, String loginId, String password) {
        this.name = name;
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
        this.address = address;
        this.userType = userType;
        this.profilePicture = profilePicture;
        this.loginId = loginId;
        this.password = password;
    }

    public void update(String name, String nickname, String gender, Integer age, String address,
        String profilePicture) {
        this.name = name;
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
        this.address = address;
        this.profilePicture = profilePicture;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }
}