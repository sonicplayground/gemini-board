package com.sonicplayground.geminiboard.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

public class UserCommand {

    @Getter
    @Builder
    @ToString
    public static class CreateUserRequest {

        private final String name;
        private final String nickname;
        private final Gender gender;
        private final Integer age;
        private final String address;
        private final UserType userType;
        private final String profilePicture;
        private final String loginId;
        private final String password;

        public User toEntity(String encodedPassword) {
            return User.builder()
                .name(name)
                .nickname(nickname)
                .gender(gender)
                .age(age)
                .address(address)
                .userType(userType)
                .profilePicture(profilePicture)
                .loginId(loginId)
                .password(encodedPassword)
                .build();
        }
    }

    @Getter
    @Builder
    @ToString
    public static class UpdateUserRequest {

        private final String nickname;
        private final String address;
        private final String profilePicture;
    }
}
