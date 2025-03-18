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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping
    public ResponseEntity<UserDto.RegisterResponse> createUser(
        @RequestBody UserDto.RegisterRequest request) {
        UUID userKey = userApplicationService.createUser(request.toCommand());
        return ResponseEntity.ok(new UserDto.RegisterResponse(userKey));
    }

    @GetMapping
    public ResponseEntity<PagedContent<UserResponse>> getUsers(
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


}
