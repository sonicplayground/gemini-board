package com.sonicplayground.geminiboard.domain.user;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 사용자 저장소 인터페이스
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLoginId(String loginId);

    Optional<User> findByNickname(String nickname);

    boolean existsByLoginId(String loginId);

    Page<User> findAll(Specification<User> spec, Pageable pageable);
}