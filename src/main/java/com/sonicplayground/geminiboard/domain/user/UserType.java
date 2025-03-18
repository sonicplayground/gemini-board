package com.sonicplayground.geminiboard.domain.user;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * 사용자 타입
 */
public enum UserType {
    SERVICE_ADMIN, // 서비스 관리자
    SERVICE_USER, // 서비스 이용자
    MAINTENANCE_ADMIN // 정비소 관리자
    ;

    @JsonCreator
    public static UserType from(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return UserType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(value + " is not valid UserType");
        }
    }
}
