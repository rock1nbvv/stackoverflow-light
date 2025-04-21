package rockinbvv.stackoverflowlight.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final EncryptionService encryptionService;

    @Transactional(readOnly = true)
    public UserResponseDto getUserById(long id) {
        return userDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.USER, "id", id));
    }

    @Transactional(readOnly = true)
    public UserFullResponseDto getAndValidateUserPassword(Long id, String rawPassword) {
        UserFullResponseDto user = userDao.findFullUserById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.USER, "id", id));

        if (user.getPassword() == null) {
            return user;
        }
        if (!encryptionService.decrypt(rawPassword, user.getPassword())) {
            throw new InvalidPasswordException();
        }

        return user;
    }

    @Transactional
    public Long createUser(UserCreateDto userDto) {
        Optional<UserResponseDto> existing = userDao.findByEmail(userDto.getEmail());
        if (existing.isPresent()) {
            throw new EmailAlreadyExistsException(userDto.getEmail());
        }

        try {
            return userDao.register(userDto);
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
