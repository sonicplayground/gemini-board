package com.sonicplayground.geminiboard.interfaces.user;

import com.sonicplayground.geminiboard.domain.user.Gender;
import com.sonicplayground.geminiboard.domain.user.User;
import com.sonicplayground.geminiboard.domain.user.UserCommand;
import com.sonicplayground.geminiboard.domain.user.UserType;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class UserDto {

    @Getter
    @ToString
    public static class RegisterResponse {

        private final UUID userKey;

        public RegisterResponse(UUID userKey) {
            this.userKey = userKey;
        }
    }

    public static class RegisterRequest {

        @NotBlank(message = "require param : name")
        private final String name;
        private final String nickname;
        @NotBlank(message = "require param : gender")
        private final Gender gender;
        @NotBlank(message = "require param : age")
        private final Integer age;
        private final String address;
        @NotBlank(message = "require param : userType")
        private final UserType userType;
        private final String profilePicture;
        @NotBlank(message = "require param : loginId")
        private final String loginId;
        @NotBlank(message = "require param : password")
        private final String password;

        public RegisterRequest(String name, String nickname, Gender gender, Integer age,
            String address, String userType, String profilePicture, String loginId,
            String password) {
            this.name = name;
            this.nickname = nickname;
            this.gender = gender;
            this.age = age;
            this.address = address;
            this.userType = UserType.from(userType);
            this.profilePicture = profilePicture;
            this.loginId = loginId;
            this.password = password;
        }

        public UserCommand.CreateUserRequest toCommand() {
            return UserCommand.CreateUserRequest.builder()
                .name(name)
                .nickname(nickname)
                .gender(gender)
                .age(age)
                .address(address)
                .userType(userType)
                .profilePicture(profilePicture)
                .loginId(loginId)
                .password(password)
                .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserSearchCondition {

        private UUID key;
        private String nickname;
        private Gender gender;
        private Integer minAge;
        private Integer maxAge;
        private UserType userType;
        private String loginId;
    }

    @Getter
    @ToString
    public static class UserResponse {

        private final UUID userKey;
        private final String name;
        private final String nickname;
        private final Gender gender;
        private final Integer age;
        private final String address;
        private final UserType userType;
        private final String profilePicture;
        private final String loginId;

        public UserResponse(User user) {
            this.userKey = user.getKey();
            this.name = user.getName();
            this.nickname = user.getNickname();
            this.gender = user.getGender();
            this.age = user.getAge();
            this.address = user.getAddress();
            this.userType = user.getUserType();
            this.profilePicture = user.getProfilePicture();
            this.loginId = user.getLoginId();
        }

    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserUpdateRequest {

        private String nickname;
        private String address;
        private String profilePicture;

        public UserCommand.UpdateUserRequest toCommand() {
            return UserCommand.UpdateUserRequest.builder()
                .nickname(nickname)
                .address(address)
                .profilePicture(profilePicture)
                .build();
        }

    }
}
