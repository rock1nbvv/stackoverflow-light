package rockinbvv.stackoverflowlight.app.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rockinbvv.stackoverflowlight.app.data.dto.user.request.UserCreateDto;
import rockinbvv.stackoverflowlight.app.data.model.User;
import rockinbvv.stackoverflowlight.app.service.UserService;
import rockinbvv.stackoverflowlight.system.crypto.EncryptionService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User")
public class UserController {

    private final UserService userService;
    private final EncryptionService encryptionService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @GetMapping("/full/{id}")
    @Description("Get user and validate its password")
    public ResponseEntity<User> getFullUserById(@PathVariable Long id, @RequestParam String password) {
        User user = userService.getUserById(id);
        if (user != null) {
            if (!encryptionService.decrypt(password, user.getPassword())) {
                return ResponseEntity.badRequest().build();
            }
            user.setPassword(password);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public User createUser(@RequestBody UserCreateDto userCreateDto) {
        return userService.saveUser(userCreateDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.disableUser(id);
        return ResponseEntity.noContent().build();
    }
}
