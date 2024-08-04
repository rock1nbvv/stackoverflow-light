package rockinbvv.stackoverflowlight.app.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rockinbvv.stackoverflowlight.app.dto.user.UserCreateDto;
import rockinbvv.stackoverflowlight.app.model.User;
import rockinbvv.stackoverflowlight.app.repository.PostRepository;
import rockinbvv.stackoverflowlight.app.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElse(null);
    }

    public User saveUser(UserCreateDto createUserDto) {
        return userRepository.save(
                User.builder()
                        .name(createUserDto.getName())
                        .email(createUserDto.getEmail())
                        .password(createUserDto.getPassword())
                        .build()
        );
    }

    @Transactional
    public void disableUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        postRepository.nullifyUserInPosts(user); //todo dont nullify but deactivate user
        userRepository.delete(user);
    }
}
