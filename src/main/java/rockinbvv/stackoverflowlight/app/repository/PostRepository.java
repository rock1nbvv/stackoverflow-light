//package rockinbvv.stackoverflowlight.app.repository;
//
//import jakarta.persistence.LockModeType;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Lock;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import rockinbvv.stackoverflowlight.app.data.Post;
//import rockinbvv.stackoverflowlight.app.data.User;
//
//public interface PostRepository extends JpaRepository<Post, Long> {
//
//    @Modifying
//    @Query("UPDATE Post p SET p.author = NULL WHERE p.author = :user")
//    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    void nullifyUserInPosts(@Param("user") User user);
//}
