package rockinbvv.stackoverflowlight.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rockinbvv.stackoverflowlight.app.dao.UserAuthDao;
import rockinbvv.stackoverflowlight.app.dao.UserDao;
import rockinbvv.stackoverflowlight.app.data.user.UserCreateDto;
import rockinbvv.stackoverflowlight.app.data.user.UserFullResponseDto;
import rockinbvv.stackoverflowlight.app.data.user.UserResponseDto;
import rockinbvv.stackoverflowlight.app.exception.EmailAlreadyExistsException;
import rockinbvv.stackoverflowlight.app.exception.EntityNotFoundException;
import rockinbvv.stackoverflowlight.app.exception.EntityType;
import rockinbvv.stackoverflowlight.app.exception.InvalidPasswordException;
import rockinbvv.stackoverflowlight.system.security.EncryptionService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
    private final UserAuthDao userAuthDao;
    private final EncryptionService encryptionService;

    @Transactional(readOnly = true)
    public UserResponseDto getUserById(long id) {
        return userDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.USER, "id", id));
    }

    @Transactional(readOnly = true)
    public UserFullResponseDto getAndValidateUserPassword(Long id, String rawPassword) {
        UserResponseDto user = userDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.USER, "id", id));

        var passwordAuthOpt = userAuthDao.findPasswordAuthByUserId(id);

        if (passwordAuthOpt.isEmpty()) {
            return userDao.findFullUserById(id)
                    .orElseThrow(() -> new EntityNotFoundException(EntityType.USER, "id", id));
        }

        if (!encryptionService.verifyPassword(rawPassword, passwordAuthOpt.get().getPassword())) {
            throw new InvalidPasswordException();
        }

        return userDao.findFullUserById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.USER, "id", id));
    }

    @Transactional(readOnly = true)
    public UserFullResponseDto authenticateUserByEmailAndPassword(String email, String rawPassword) {
        UserResponseDto user = userDao.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.USER, "email", email));

        if (user.getIsAdmin() == null || !user.getIsAdmin()) {
            throw new InvalidPasswordException("Only admin users can login with password");
        }

        var passwordAuth = userAuthDao.findPasswordAuthByUserId(user.getId())
                .orElseThrow(() -> new InvalidPasswordException("User does not have a password"));

        if (!encryptionService.verifyPassword(rawPassword, passwordAuth.getPassword())) {
            throw new InvalidPasswordException();
        }

        return userDao.findFullUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.USER, "email", email));
    }

    @Transactional
    public Long createUser(UserCreateDto userDto) {
        Optional<UserResponseDto> existing = userDao.findByEmail(userDto.getEmail());
        if (existing.isPresent()) {
            throw new EmailAlreadyExistsException(userDto.getEmail());
        }

        try {
            Long userId = userDao.register(userDto);

            if (userDto.getPassword() != null) {
                userAuthDao.createPasswordAuth(userId, userDto.getPassword());
            }

            if (userDto.getGoogleId() != null) {
                userAuthDao.createGoogleAuth(userId, userDto.getGoogleId());
            }

            return userId;
        } catch (DuplicateKeyException e) {
            throw new EmailAlreadyExistsException(userDto.getEmail());
        }
    }

    @Transactional
    public void deactivateUserById(long id) {
        boolean exists = userDao.findById(id).isPresent();
        if (!exists) {
            throw new EntityNotFoundException(EntityType.USER, "id", id);
        }

        userDao.corruptById(id);
    }
}
