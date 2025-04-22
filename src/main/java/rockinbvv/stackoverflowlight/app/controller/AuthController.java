package rockinbvv.stackoverflowlight.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rockinbvv.stackoverflowlight.app.data.user.LoginRequestDto;
import rockinbvv.stackoverflowlight.app.data.user.UserFullResponseDto;
import rockinbvv.stackoverflowlight.app.service.UserService;
import rockinbvv.stackoverflowlight.system.ResponseWrapper;
import rockinbvv.stackoverflowlight.system.security.AuthenticatedUser;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ResponseWrapper<String>> login(@RequestBody LoginRequestDto loginRequest) {
        // Authenticate the user with email and password
        UserFullResponseDto user = userService.authenticateUserByEmailAndPassword(
                loginRequest.getEmail(), 
                loginRequest.getPassword()
        );

        // Create authorities collection with admin role
        var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));

        // Create authentication token with admin role
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                new AuthenticatedUser(user.getId(), user.getEmail(), authorities),
                null,
                authorities
        );

        // Set authentication in security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.ok(ResponseWrapper.<String>builder()
                .data("Login successful")
                .build());
    }
}
