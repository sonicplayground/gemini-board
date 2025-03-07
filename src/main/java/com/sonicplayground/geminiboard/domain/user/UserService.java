package com.sonicplayground.geminiboard.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 사용자 비즈니스 로직
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;

    @Transactional
    public User createUser(User user){
        userValidator.validateUser(user);
        user.passwordEncode(passwordEncoder);
        return userRepository.save(user);
    }
    @Transactional
    public User updateUser(User user){
        userValidator.validateUpdateUser(user);
        return userRepository.save(user);
    }
    @Transactional
    public void updatePassword(User user, String newPassword){
        userValidator.validateUpdatePassword(user);
        user.updatePassword(newPassword);
        user.passwordEncode(passwordEncoder);
        userRepository.save(user);
    }
    public User findUser(Long id){
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }
}