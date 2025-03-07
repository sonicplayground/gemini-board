package com.sonicplayground.geminiboard.domain.user;

import org.springframework.stereotype.Component;
import java.util.Optional;

/**
 * 사용자 유효성 검증
 */
@Component
public class UserValidator {
    private final UserRepository userRepository;

    public UserValidator(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public void validateUser(User user) {
        Optional<User> findUser = userRepository.findByLoginId(user.getLoginId());
        if (findUser.isPresent()){
            throw new IllegalArgumentException("이미 존재하는 로그인 아이디 입니다.");
        }
        Optional<User> findNickname = userRepository.findByNickname(user.getNickname());
        if(findNickname.isPresent()){
            throw new IllegalArgumentException("이미 존재하는 닉네임 입니다.");
        }
    }
    public void validateUpdateUser(User user) {
        Optional<User> findNickname = userRepository.findByNickname(user.getNickname());
        if(findNickname.isPresent() && !findNickname.get().getId().equals(user.getId())){
            throw new IllegalArgumentException("이미 존재하는 닉네임 입니다.");
        }
    }
    public void validateUpdatePassword(User user) {
        if(user.getPassword() == null || user.getPassword().equals("")){
            throw new IllegalArgumentException("비밀번호는 필수 값입니다.");
        }
    }
}