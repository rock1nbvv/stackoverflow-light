package rockinbvv.stackoverflowlight.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rockinbvv.stackoverflowlight.app.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
