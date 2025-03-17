package com.sonicplayground.geminiboard.application.user;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sonicplayground.geminiboard.domain.user.Gender;
import com.sonicplayground.geminiboard.domain.user.UserCommand;
import com.sonicplayground.geminiboard.domain.user.UserService;
import com.sonicplayground.geminiboard.domain.user.UserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserApplicationServiceTest {

    @InjectMocks
    private UserApplicationService userApplicationService;

    @Mock
    private UserService userService;

    @Test
    @DisplayName("사용자 생성 테스트")
    void createUserTest() {
        // given
        UserCommand.CreateUserRequest request = UserCommand.CreateUserRequest.builder()
            .name("홍길동")
            .nickname("동대장")
            .gender(Gender.MALE)
            .age(20)
            .address("서천군")
            .userType(UserType.SERVICE_USER)
            .profilePicture(null)
            .loginId("hkdfollowme")
            .password("qweqwe123!")
            .build();

        when(userService.createUser(any())).thenReturn(request.toEntity());

        // when
        String userKey = userApplicationService.createUser(request);

        // then
        assertNotNull(userKey);
    }
}