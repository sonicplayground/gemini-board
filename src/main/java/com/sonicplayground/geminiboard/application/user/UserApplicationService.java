package com.sonicplayground.geminiboard.application.user;

import com.sonicplayground.geminiboard.domain.user.AuthService;
import com.sonicplayground.geminiboard.domain.user.User;
import com.sonicplayground.geminiboard.domain.user.UserCommand;
import com.sonicplayground.geminiboard.domain.user.UserService;
import com.sonicplayground.geminiboard.interfaces.user.LoginDto;
import com.sonicplayground.geminiboard.interfaces.user.UserDto.UserResponse;
import com.sonicplayground.geminiboard.interfaces.user.UserDto.UserSearchCondition;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserApplicationService {

    private final UserService userService;
    private final AuthService authService;

    public UUID createUser(UserCommand.CreateUserRequest request) {
        log.debug("createUser request info: {}", request.toString());

        userService.checkLoginIdDuplicate(request.getLoginId());

        User created = userService.createUser(request);
        log.debug("created user info: {}", created.toString());
        return created.getKey();
    }

    public Page<UserResponse> getUsers(UserSearchCondition condition, Pageable pageable) {
        Page<User> users = userService.retrieveUsers(condition, pageable);
        return users.map(UserResponse::new);
    }


    public UserResponse updateUser(LoginDto.RequesterInfo requester, UUID userKey,
        UserCommand.UpdateUserRequest request) {
        authService.checkAuthority(requester, userKey);
        User updatedUser = userService.updateUser(userKey, request);
        return new UserResponse(updatedUser);
    }

    public void deleteUser(LoginDto.RequesterInfo requester, UUID userKey) {
        authService.checkAuthority(requester, userKey);
        userService.deleteUser(userKey);
    }

    public UserResponse getUser(LoginDto.RequesterInfo requester, UUID userKey) {
        authService.checkAuthority(requester, userKey);
        User user = userService.getUserByUserKey(userKey);
        return new UserResponse(user);
    }
}
