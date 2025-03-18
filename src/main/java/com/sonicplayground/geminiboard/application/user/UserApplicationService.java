package com.sonicplayground.geminiboard.application.user;

import com.sonicplayground.geminiboard.domain.user.User;
import com.sonicplayground.geminiboard.domain.user.UserCommand;
import com.sonicplayground.geminiboard.domain.user.UserService;
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


    public UserResponse updateUser(UUID userKey, UserCommand.UpdateUserRequest request) {
        User updatedUser = userService.updateUser(userKey, request);
        return new UserResponse(updatedUser);
    }

    public void deleteUser(UUID userKey) {
        userService.deleteUser(userKey);
    }
}
