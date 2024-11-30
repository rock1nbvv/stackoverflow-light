package rockinbvv.stackoverflowlight.app.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import rockinbvv.stackoverflowlight.app.data.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByEmail(@NotNull String email);

    Optional<User> findOneByEmailEqualsIgnoreCase(@NotNull String email);
}
