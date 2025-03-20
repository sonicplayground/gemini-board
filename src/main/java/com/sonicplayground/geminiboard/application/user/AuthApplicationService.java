package com.sonicplayground.geminiboard.application.user;

import com.sonicplayground.geminiboard.domain.user.AuthService;
import com.sonicplayground.geminiboard.domain.user.User;
import com.sonicplayground.geminiboard.domain.user.UserService;
import com.sonicplayground.geminiboard.interfaces.user.LoginDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthApplicationService {

    private final AuthService authService;
    private final UserService userService;
    public LoginDto.SignInResponse signIn(String loginId, String password) {
        log.debug("signIn request info: loginId={}, password={}", loginId, password);

        User target = userService.getUserByLoginId(loginId);

        return authService.signIn(target, password);
    }
}
