package com.sonicplayground.geminiboard.domain.user;

import com.sonicplayground.geminiboard.interfaces.user.UserDto.UserSearchCondition;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 사용자 저장소 인터페이스
 */
public interface UserReader {

    Optional<User> findByLoginId(String loginId);

    Optional<User> findByKey(UUID loginId);

    boolean existsByLoginId(String loginId);

    Page<User> findAll(UserSearchCondition spec, Pageable pageable);
}