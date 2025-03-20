package com.sonicplayground.geminiboard.domain.user;

import com.sonicplayground.geminiboard.infrastructure.security.JwtTokenProvider;
import com.sonicplayground.geminiboard.interfaces.user.LoginDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    public LoginDto.SignInResponse signIn(User target, String password) {
        String answer = target.getPassword();
        if (!passwordEncoder.matches(password, answer)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String token = tokenProvider.createToken(
            String.format("%s:%s", target.getKey(), target.getUserType()));
        log.debug("token: {}", token);

        return new LoginDto.SignInResponse(token);
    }

    public void checkAuthority(LoginDto.RequesterInfo requester, UUID targetKey) {
        if (UserType.SERVICE_ADMIN.equals(requester.getUserType())) {
            return;
        }

        if (requester.getUserKey().equals(targetKey)) {
            return;
        }

        throw new IllegalArgumentException("권한이 없습니다.");
    }
}
