package rockinbvv.stackoverflowlight.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rockinbvv.stackoverflowlight.app.model.Answer;
import rockinbvv.stackoverflowlight.app.model.Post;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {


    List<Answer> findAnswersByPost(Post post);//todo change to postId

    @Query("SELECT a " +
            "FROM Answer a " +
            "JOIN FETCH a.author " +
            "WHERE a.post.id = :postId")
    List<Answer> findByPostId(@Param("postId") Long postId);
}
