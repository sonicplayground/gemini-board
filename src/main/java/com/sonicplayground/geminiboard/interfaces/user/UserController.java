package com.sonicplayground.geminiboard.interfaces.user;

import com.sonicplayground.geminiboard.application.user.UserApplicationService;
import com.sonicplayground.geminiboard.common.response.PagedContent;
import com.sonicplayground.geminiboard.domain.user.Gender;
import com.sonicplayground.geminiboard.domain.user.UserType;
import com.sonicplayground.geminiboard.interfaces.user.UserDto.UserResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/users")
public class UserController {

    private final UserApplicationService userApplicationService;

    @GetMapping
    @PreAuthorize("hasAuthority('SERVICE_ADMIN')")
    public ResponseEntity<PagedContent<UserDto.UserResponse>> getUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) String nickname,
        @RequestParam(required = false) String gender,
        @RequestParam(required = false) Integer minAge,
        @RequestParam(required = false) Integer maxAge,
        @RequestParam(required = false) String userType
    ) {
        Pageable pageable = PageRequest.of(page, size);
        UserDto.UserSearchCondition condition = UserDto.UserSearchCondition.builder()
            .nickname(nickname)
            .gender(Gender.from(gender))
            .minAge(minAge)
            .maxAge(maxAge)
            .userType(UserType.from(userType))
            .build();
        Page<UserDto.UserResponse> users = userApplicationService.getUsers(condition, pageable);
        PagedContent<UserDto.UserResponse> result = new PagedContent<>(users);

        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/{userKey}")
    public ResponseEntity<UserDto.UserResponse> getUser(@AuthenticationPrincipal User requester,
        @PathVariable String userKey) {
        LoginDto.RequesterInfo requesterInfo = LoginDto.RequesterInfo.from(requester);
        UserResponse user = userApplicationService.getUser(requesterInfo, UUID.fromString(userKey));
        return ResponseEntity.ok(user);
    }

    @PutMapping(value = "/{userKey}")
    public ResponseEntity<UserDto.UserResponse> updateUser(
        @AuthenticationPrincipal User requester,
        @RequestBody UserDto.UserUpdateRequest request,
        @PathVariable String userKey) {
        LoginDto.RequesterInfo requesterInfo = LoginDto.RequesterInfo.from(requester);
        UserDto.UserResponse updatedUser = userApplicationService.updateUser(requesterInfo,
            UUID.fromString(userKey), request.toCommand());
        return ResponseEntity.ok(updatedUser);
    }


    @DeleteMapping(value = "/{userKey}")
    public ResponseEntity<UserDto.DeleteResponse> deleteUser(
        @AuthenticationPrincipal User requester, @PathVariable String userKey) {
        LoginDto.RequesterInfo requesterInfo = LoginDto.RequesterInfo.from(requester);
        userApplicationService.deleteUser(requesterInfo, UUID.fromString(userKey));
        UserDto.DeleteResponse response = new UserDto.DeleteResponse("User deleted successfully");
        return ResponseEntity.ok(response);
    }


}
