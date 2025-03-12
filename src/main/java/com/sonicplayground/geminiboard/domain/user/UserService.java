package com.sonicplayground.geminiboard.domain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public User findUser(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }
}