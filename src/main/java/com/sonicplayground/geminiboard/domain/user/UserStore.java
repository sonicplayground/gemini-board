package com.sonicplayground.geminiboard.domain.user;

/**
 * 사용자 저장소 인터페이스
 */
public interface UserStore {

    User save(User newUser);

    void delete(User user);
}