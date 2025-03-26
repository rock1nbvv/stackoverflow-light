package rockinbvv.stackoverflowlight.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rockinbvv.stackoverflowlight.app.exception.EmailAlreadyExistsException;
import rockinbvv.stackoverflowlight.app.exception.InvalidPasswordException;
import rockinbvv.stackoverflowlight.app.exception.UserNotFoundException;
import rockinbvv.stackoverflowlight.app.dao.UserDao;
import rockinbvv.stackoverflowlight.app.data.User;
import rockinbvv.stackoverflowlight.app.data.dto.user.request.CreateUserDto;
import rockinbvv.stackoverflowlight.system.crypto.EncryptionService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
    private final EncryptionService encryptionService;

    @Transactional(readOnly = true)
    public User getUserById(long id) {
        return userDao.findOneById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public User getAndValidateUserPassword(Long id, String rawPassword) {
        User user = userDao.findOneById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (!encryptionService.decrypt(rawPassword, user.getPassword())) {
            throw new InvalidPasswordException();
        }

        user.setPassword(rawPassword);
        return user;
    }

    @Transactional
    public Long createUser(CreateUserDto userDto) {
        Optional<User> existing = userDao.findOneByEmail(userDto.getEmail());
        if (existing.isPresent()) {
            throw new EmailAlreadyExistsException(userDto.getEmail());
        }

        try {
            return userDao.registerUser(userDto);
        } catch (DuplicateKeyException e) {
            throw new EmailAlreadyExistsException(userDto.getEmail());
        }
    }

    @Transactional
    public void deactivateUserById(long userId) {
        boolean exists = userDao.findOneById(userId).isPresent();
        if (!exists) {
            throw new UserNotFoundException(userId);
        }

        userDao.corruptById(userId);
    }
}
