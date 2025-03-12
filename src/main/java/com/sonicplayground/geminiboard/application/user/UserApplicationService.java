package com.sonicplayground.geminiboard.application.user;

import com.sonicplayground.geminiboard.domain.user.User;
import com.sonicplayground.geminiboard.domain.user.UserCommand;
import com.sonicplayground.geminiboard.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserApplicationService {

    private final UserService userService;

    public String createUser(UserCommand.CreateUserRequest request) {
        User created = userService.createUser(request);
        return created.getUuid();
    }
}
