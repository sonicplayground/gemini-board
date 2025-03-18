package com.sonicplayground.geminiboard.infrastructure.user;

import com.sonicplayground.geminiboard.domain.user.Gender;
import com.sonicplayground.geminiboard.domain.user.User;
import com.sonicplayground.geminiboard.domain.user.UserReader;
import com.sonicplayground.geminiboard.domain.user.UserType;
import com.sonicplayground.geminiboard.interfaces.user.UserDto.UserSearchCondition;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserReaderImpl implements UserReader {

    private final UserJpaRepository userRepository;

    public static Specification<User> searchUsers(String name, String loginId, UUID key,
        Integer minAge, Integer maxAge, Gender gender, UserType userType) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 이름 검색 (LIKE 연산)
            if (name != null && !name.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            // key 검색 (Equals 연산)
            if (key != null) {
                predicates.add(cb.equal(root.get("user_key"), key));
            }

            // 로그인 ID 검색 (Equals)
            if (loginId != null && !loginId.isEmpty()) {
                predicates.add(cb.equal(root.get("loginId"), loginId));
            }

            // 나이 범위 검색 (Between)
            if (minAge != null && maxAge != null) {
                predicates.add(cb.between(root.get("age"), minAge, maxAge));
            } else if (minAge != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("age"), minAge));
            } else if (maxAge != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("age"), maxAge));
            }

            // 성별 검색 (Equals)
            if (gender != null) {
                predicates.add(cb.equal(root.get("gender"), gender));
            }

            // 사용자 타입 검색 (Equals)
            if (userType != null) {
                predicates.add(cb.equal(root.get("userType"), userType));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    public Optional<User> findByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId);
    }

    @Override
    public boolean existsByLoginId(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    @Override
    public Page<User> findAll(UserSearchCondition condition, Pageable pageable) {
        Specification<User> spec = searchUsers(condition.getNickname(), condition.getLoginId(),
            condition.getKey(), condition.getMinAge(), condition.getMaxAge(), condition.getGender(),
            condition.getUserType());
        return userRepository.findAll(spec, pageable);
    }
}
