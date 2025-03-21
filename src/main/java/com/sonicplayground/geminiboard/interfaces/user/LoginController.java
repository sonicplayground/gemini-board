package com.sonicplayground.geminiboard.interfaces.user;

import com.sonicplayground.geminiboard.application.user.AuthApplicationService;
import com.sonicplayground.geminiboard.application.user.UserApplicationService;
import com.sonicplayground.geminiboard.interfaces.user.LoginDto.SignInResponse;
import com.sonicplayground.geminiboard.interfaces.user.UserDto.RegisterResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/login")
public class LoginController {

    private final UserApplicationService userApplicationService;
    private final AuthApplicationService authApplicationService;

    @PostMapping("/sign-up")
    public ResponseEntity<RegisterResponse> createUser(
        @RequestBody UserDto.RegisterRequest request) {
        UUID userKey = userApplicationService.createUser(request.toCommand());
        return ResponseEntity.ok(new UserDto.RegisterResponse(userKey));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<LoginDto.SignInResponse> signIn(
        @RequestBody LoginDto.SingInRequest request) {
        SignInResponse result = authApplicationService.signIn(request.getLoginId(),
            request.getPassword());
        return ResponseEntity.ok(result);
    }
}
