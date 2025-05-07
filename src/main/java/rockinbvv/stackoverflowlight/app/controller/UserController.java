package rockinbvv.stackoverflowlight.app.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rockinbvv.stackoverflowlight.app.data.user.UserCreateDto;
import rockinbvv.stackoverflowlight.app.data.user.UserFullResponseDto;
import rockinbvv.stackoverflowlight.app.data.user.UserResponseDto;
import rockinbvv.stackoverflowlight.app.service.UserService;
import rockinbvv.stackoverflowlight.system.ResponseWrapper;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User")
@PreAuthorize("hasAuthority('OIDC_USER') or hasAuthority('LOCAL_USER')")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseWrapper<UserResponseDto> getUserById(@PathVariable Long id) {
        return ResponseWrapper.ok(userService.getUserById(id));
    }

    @GetMapping("/{id}/full")
    @Description("Get user and validate its password")
    public ResponseWrapper<UserFullResponseDto> getFullUserById(@PathVariable Long id, @RequestParam String password) {
        UserFullResponseDto user = userService.getAndValidateUserPassword(id, password);
        return ResponseWrapper.ok(user);
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWrapper<Long> createUser(@RequestBody UserCreateDto dto) {
        return ResponseWrapper.ok(userService.createUser(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseWrapper deactivateUser(@PathVariable long id) {
        userService.deactivateUserById(id);
        return ResponseWrapper.ok();
    }
}
