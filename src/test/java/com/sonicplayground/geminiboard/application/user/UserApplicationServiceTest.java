package com.sonicplayground.geminiboard.application.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sonicplayground.geminiboard.domain.user.AuthService;
import com.sonicplayground.geminiboard.domain.user.User;
import com.sonicplayground.geminiboard.domain.user.UserCommand;
import com.sonicplayground.geminiboard.domain.user.UserService;
import com.sonicplayground.geminiboard.domain.user.UserType;
import com.sonicplayground.geminiboard.interfaces.user.LoginDto;
import com.sonicplayground.geminiboard.interfaces.user.UserDto.UserResponse;
import com.sonicplayground.geminiboard.interfaces.user.UserDto.UserSearchCondition;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class UserApplicationServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private AuthService authService;

    @InjectMocks
    private UserApplicationService userApplicationService;

    @Test
    @DisplayName("createUser - Success")
    void createUser_Success() {
        // Given
        UserCommand.CreateUserRequest request = UserCommand.CreateUserRequest
            .builder()
            .loginId("testLoginId")
            .password("testPassword")
            .nickname("testNickname")
            .build();
        User createdUser = User.builder()
            .key(UUID.randomUUID())
            .loginId("testLoginId")
            .password("testPassword")
            .nickname("testNickname")
            .build();

        when(userService.createUser(request)).thenReturn(createdUser);

        // When
        UUID result = userApplicationService.createUser(request);

        // Then
        assertNotNull(result);
        assertEquals(createdUser.getKey(), result);
        verify(userService, times(1)).checkLoginIdDuplicate("testLoginId");
        verify(userService, times(1)).createUser(request);
    }

    @Test
    @DisplayName("createUser - LoginId Duplicate")
    void createUser_LoginIdDuplicate() {
        // Given
        UserCommand.CreateUserRequest request = UserCommand.CreateUserRequest
            .builder()
            .name("duplicateLoginId")
            .password("testPassword")
            .nickname("testNickname")
            .build();

        doThrow(new RuntimeException("Login ID already exists")).when(userService)
            .checkLoginIdDuplicate(request.getLoginId());

        //when
        assertThrows(RuntimeException.class, () -> userApplicationService.createUser(request));

        //then
        verify(userService, times(1)).checkLoginIdDuplicate(request.getLoginId());
        verify(userService, never()).createUser(any());
    }

    @Test
    @DisplayName("getUsers - Success")
    void getUsers_Success() {
        // Given
        UserSearchCondition condition = new UserSearchCondition();
        Pageable pageable = PageRequest.of(0, 10);
        User user1 = User.builder()
            .key(UUID.randomUUID())
            .loginId("testLoginId1")
            .password("testPassword1")
            .nickname("testNickname1")
            .build();
        User user2 = User.builder()
            .key(UUID.randomUUID())
            .loginId("testLoginId2")
            .password("testPassword2")
            .nickname("testNickname2")
            .build();
        List<User> users = List.of(user1, user2);
        Page<User> userPage = new PageImpl<>(users, pageable, users.size());

        when(userService.retrieveUsers(condition, pageable)).thenReturn(userPage);

        // When
        Page<UserResponse> result = userApplicationService.getUsers(condition, pageable);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals("testLoginId1", result.getContent().get(0).getLoginId());
        assertEquals("testNickname2", result.getContent().get(1).getNickname());
        verify(userService, times(1)).retrieveUsers(condition, pageable);
    }

    @Test
    @DisplayName("getUsers - Empty List")
    void getUsers_EmptyList() {
        // Given
        UserSearchCondition condition = new UserSearchCondition();
        Pageable pageable = PageRequest.of(0, 10);
        List<User> users = List.of();
        Page<User> userPage = new PageImpl<>(users, pageable, 0);

        when(userService.retrieveUsers(condition, pageable)).thenReturn(userPage);

        // When
        Page<UserResponse> result = userApplicationService.getUsers(condition, pageable);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getContent().size());
        verify(userService, times(1)).retrieveUsers(condition, pageable);
    }

    @Test
    @DisplayName("updateUser - Success")
    void updateUser_Success() {
        // Given
        UUID userKey = UUID.randomUUID();
        UserCommand.UpdateUserRequest request = UserCommand.UpdateUserRequest.builder()
            .nickname("newNickname")
            .profilePicture("newProfilePicture")
            .address("newAddress")
            .build();
        User updatedUser = User.builder()
            .key(userKey)
            .loginId("testLoginId")
            .password("newPassword")
            .nickname("newNickname")
            .profilePicture("newProfilePicture")
            .address("newAddress")
            .build();


        // When
        LoginDto.RequesterInfo requester = new LoginDto.RequesterInfo(userKey, UserType.SERVICE_ADMIN);
        when(userService.updateUser(userKey, request)).thenReturn(updatedUser);
        doNothing().when(authService).checkAuthority(requester, userKey);
        UserResponse result = userApplicationService.updateUser(requester, userKey, request);

        // Then
        assertNotNull(result);
        assertEquals("newNickname", result.getNickname());
        assertEquals("newProfilePicture", result.getProfilePicture());
        assertEquals("newAddress", result.getAddress());
        verify(userService, times(1)).updateUser(userKey, request);
    }

    @Test
    @DisplayName("updateUser - User Not Found")
    void updateUser_UserNotFound() {
        // Given
        UUID userKey = UUID.randomUUID();
        UserCommand.UpdateUserRequest request = UserCommand.UpdateUserRequest.builder()
            .nickname("newNickName")
            .profilePicture("newProfilePicture")
            .address("newAddress")
            .build();


        // When
        doThrow(new RuntimeException("User not found")).when(userService)
            .updateUser(userKey, request);

        LoginDto.RequesterInfo requester = new LoginDto.RequesterInfo(userKey, UserType.SERVICE_USER);
        doNothing().when(authService).checkAuthority(requester, userKey);

        // When & Then
        assertThrows(RuntimeException.class,
            () -> userApplicationService.updateUser(requester, userKey, request));
        verify(userService, times(1)).updateUser(userKey, request);
    }

    @Test
    @DisplayName("deleteUser - Success")
    void deleteUser_Success() {
        // Given
        UUID userKey = UUID.randomUUID();

        LoginDto.RequesterInfo requester = new LoginDto.RequesterInfo(userKey, UserType.SERVICE_USER);
        doNothing().when(authService).checkAuthority(requester, userKey);

        // When
        userApplicationService.deleteUser(requester, userKey);

        // Then
        verify(userService, times(1)).deleteUser(userKey);
    }

    @Test
    @DisplayName("deleteUser - User Not Found")
    void deleteUser_UserNotFound() {
        // Given
        UUID userKey = UUID.randomUUID();
        doThrow(new RuntimeException("User not found")).when(userService).deleteUser(userKey);

        LoginDto.RequesterInfo requester = new LoginDto.RequesterInfo(userKey, UserType.SERVICE_ADMIN);
        // When & Then
        doNothing().when(authService).checkAuthority(requester, userKey);
        assertThrows(RuntimeException.class, () -> userApplicationService.deleteUser(requester, userKey));
        verify(userService, times(1)).deleteUser(userKey);
    }


    @Test
    @DisplayName("getUser - Success")
    void getUser_Success() {
        // Given
        UUID userKey = UUID.randomUUID();
        User user = User.builder()
            .key(userKey)
            .loginId("testLoginId")
            .password("testPassword")
            .nickname("testNickname")
            .build();

        // When
        LoginDto.RequesterInfo requester = new LoginDto.RequesterInfo(userKey, UserType.SERVICE_ADMIN);
        when(userService.getUserByUserKey(userKey)).thenReturn(user);
        doNothing().when(authService).checkAuthority(requester, userKey);
        UserResponse result = userApplicationService.getUser(requester, userKey);

        // Then
        assertNotNull(result);
        assertEquals("testLoginId", result.getLoginId());
        assertEquals("testNickname", result.getNickname());
        verify(userService, times(1)).getUserByUserKey(userKey);
    }

    @Test
    @DisplayName("getUser - User Not Found")
    void getUser_UserNotFound() {
        // Given
        UUID userKey = UUID.randomUUID();
        doThrow(new RuntimeException("User not found")).when(userService).getUserByUserKey(userKey);

        // When & Then
        LoginDto.RequesterInfo requester = new LoginDto.RequesterInfo(userKey, UserType.SERVICE_ADMIN);

        doNothing().when(authService).checkAuthority(requester, userKey);
        assertThrows(RuntimeException.class, () -> userApplicationService.getUser(requester, userKey));
        verify(userService, times(1)).getUserByUserKey(userKey);
    }

}