package rockinbvv.stackoverflowlight.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rockinbvv.stackoverflowlight.app.model.Post;
import rockinbvv.stackoverflowlight.app.model.User;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Modifying
    @Query("UPDATE Post p SET p.id_author = NULL WHERE p.id_author = :user")
    void nullifyUserInPosts(@Param("user") User user);
}