package com.sonicplayground.geminiboard.domain.user;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * 사용자 성별
 */
public enum Gender {
    MALE, // 남성
    FEMALE, // 여성
    ETC // 그외
    ;

    @JsonCreator
    public static Gender from(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return Gender.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(value + " is not valid Gender");
        }
    }
}
