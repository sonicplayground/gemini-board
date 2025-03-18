package com.sonicplayground.geminiboard.domain.user;

import com.sonicplayground.geminiboard.interfaces.user.UserDto.UserSearchCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 사용자 비즈니스 로직
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserReader userReader;
    private final UserStore userStore;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(UserCommand.CreateUserRequest user) {
        User newUser = user.toEntity();
        newUser.passwordEncode(passwordEncoder);
        return userStore.save(newUser);
    }

    public Page<User> retrieveUsers(UserSearchCondition condition, Pageable pageable) {

        return userReader.findAll(condition, pageable);
    }

    public void checkLoginIdDuplicate(String loginId) {
        boolean exists = userReader.existsByLoginId(loginId);
        if (exists) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

    }

}