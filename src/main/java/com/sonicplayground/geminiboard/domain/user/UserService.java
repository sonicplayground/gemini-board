package com.sonicplayground.geminiboard.domain.user;

import com.sonicplayground.geminiboard.interfaces.user.UserDto.UserSearchCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(UserCommand.CreateUserRequest user) {
        User newUser = user.toEntity();
        newUser.passwordEncode(passwordEncoder);
        return userRepository.save(newUser);
    }

    @Transactional
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public void updatePassword(User user, String newPassword) {
        user.updatePassword(newPassword);
        user.passwordEncode(passwordEncoder);
        userRepository.save(user);
    }

    public User findUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

    public void retrieveUsers(UserSearchCondition condition, Pageable pageable) {
//        function to retrieve user list with condition and pagination


    }

    public void checkLoginIdDuplicate(String loginId) {
        boolean exists = userRepository.existsByLoginId(loginId);
        if (exists) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

    }
}