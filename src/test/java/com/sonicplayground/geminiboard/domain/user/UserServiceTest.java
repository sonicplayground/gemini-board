package com.sonicplayground.geminiboard.domain.user;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sonicplayground.geminiboard.interfaces.user.UserDto.UserSearchCondition;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserStore userStore;

    @Mock
    private UserReader userReader;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserCommand.CreateUserRequest mockRequest;
    private User mockUser;
    private UUID userKey;

    @BeforeEach
    void setUp() {
        userKey = UUID.randomUUID();
        mockRequest = UserCommand.CreateUserRequest.builder()
            .name("testUser")
            .nickname("testUser")
            .gender(Gender.MALE)
            .birth(LocalDateTime.now())
            .address("testAddress")
            .userType(UserType.SERVICE_USER)
            .profilePicture("testPicture")
            .loginId("testUser")
            .password("password123")
            .build();
        mockUser = User.builder()
            .key(userKey)
            .name("testUser")
            .nickname("testUser")
            .gender(Gender.MALE)
            .birth(LocalDateTime.now().minusYears(10))
            .address("testAddress")
            .userType(UserType.SERVICE_USER)
            .profilePicture("testPicture")
            .loginId("testUser")
            .password("encodedPassword123")
            .build();
    }

    @Test
    void createUser_Success() {
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword123");
        when(userStore.save(any(User.class))).thenReturn(mockUser);

        User createdUser = userService.createUser(mockRequest);

        assertNotNull(createdUser);
        assertEquals("testUser", createdUser.getLoginId());
        verify(userStore).save(any(User.class));
        verify(passwordEncoder).encode("password123");
    }

    @Test
    void retrieveUsers_Success_NoCondition() {
        Pageable pageable = mock(Pageable.class);
        UserSearchCondition condition = new UserSearchCondition();

        List<User> userList = List.of(mockUser);
        Page<User> mockPage = new PageImpl<>(userList, pageable, userList.size());

        when(userReader.findAll(condition, pageable)).thenReturn(mockPage);

        Page<User> result = userService.retrieveUsers(condition, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(userReader).findAll(condition, pageable);
    }

    @Test
    void checkLoginIdDuplicate_WhenDuplicate_ThrowsException() {
        String loginId = "testUser";
        when(userReader.existsByLoginId(loginId)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.checkLoginIdDuplicate(loginId);
        });

        assertEquals("이미 존재하는 아이디입니다.", exception.getMessage());
        verify(userReader).existsByLoginId(loginId);
    }

    @Test
    void getUserByUserKey_Success() {
        when(userReader.findByKey(userKey)).thenReturn(Optional.of(mockUser));

        User result = userService.getUserByUserKey(userKey);

        assertNotNull(result);
        assertEquals("testUser", result.getLoginId());
        verify(userReader).findByKey(userKey);
    }

    @Test
    void getUserByUserKey_NotFound_ThrowsException() {
        UUID unknownKey = UUID.randomUUID();
        when(userReader.findByKey(unknownKey)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.getUserByUserKey(unknownKey);
        });

        assertEquals("유저를 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    void deleteUser_Success() {
        when(userReader.findByKey(userKey)).thenReturn(Optional.of(mockUser));

        assertDoesNotThrow(() -> userService.deleteUser(userKey));
        verify(userStore).delete(mockUser);
    }

    @Test
    void deleteUser_NotFound_ThrowsException() {
        UUID unknownKey = UUID.randomUUID();
        when(userReader.findByKey(unknownKey)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.deleteUser(unknownKey);
        });

        assertEquals("유저를 찾을 수 없습니다.", exception.getMessage());
    }
}
