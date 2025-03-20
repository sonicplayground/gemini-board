package com.sonicplayground.geminiboard.domain.user;

import com.sonicplayground.geminiboard.domain.user.UserCommand.UpdateUserRequest;
import com.sonicplayground.geminiboard.interfaces.user.UserDto.UserSearchCondition;
import java.util.UUID;
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
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        User newUser = user.toEntity(encodedPassword);
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

    public User getUserByUserKey(UUID userKey) {
        return userReader.findByKey(userKey)
            .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    }

    public User getUserByLoginId(String loginId) {
        return userReader.findByLoginId(loginId)
            .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    }

    @Transactional
    public User updateUser(UUID userKey, UpdateUserRequest request) {
        User user = userReader.findByKey(userKey)
            .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        user.update(request.getNickname(), request.getAddress(), request.getProfilePicture());

        return user;
    }

    @Transactional
    public void deleteUser(UUID userKey) {
        User user = userReader.findByKey(userKey)
            .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        userStore.delete(user);
    }
}