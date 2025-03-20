package com.sonicplayground.geminiboard.interfaces.user;

import com.sonicplayground.geminiboard.domain.user.UserType;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class LoginDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SingInRequest {

        private String loginId;
        private String password;

    }

    @Getter
    @ToString
    public static class SignInResponse {

        private final String token;

        public SignInResponse(String token) {
            this.token = token;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class RequesterInfo {

        private UUID userKey;
        private UserType userType;

        public static RequesterInfo from(User user) {
            Optional<? extends GrantedAuthority> authorityOptional = user.getAuthorities().stream()
                .findFirst();

            if (authorityOptional.isEmpty()) {
                throw new AuthorizationDeniedException("User has no assigned authority.");
            }

            String auth = authorityOptional.get().getAuthority();

            try {
                return new RequesterInfo(UUID.fromString(user.getUsername()), UserType.from(auth));
            } catch (IllegalArgumentException e) {
                throw new AuthorizationDeniedException("Invalid user authority: " + auth);
            }
        }
    }
}
