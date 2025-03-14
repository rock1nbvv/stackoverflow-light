package rockinbvv.stackoverflowlight.app.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import rockinbvv.stackoverflowlight.app.data.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByEmail(@NotNull String email);

    Optional<User> findOneByEmailEqualsIgnoreCase(@NotNull String email);

    Optional<User> findOneByGoogleId(@NotNull String googleId);
}
