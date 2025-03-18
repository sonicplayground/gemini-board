package com.sonicplayground.geminiboard.infrastructure.user;

import com.sonicplayground.geminiboard.domain.user.User;
import com.sonicplayground.geminiboard.domain.user.UserStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserStoreImpl implements UserStore {

    private final UserJpaRepository userRepository;

    public User save(User newUser) {
        return userRepository.save(newUser);
    }


}
