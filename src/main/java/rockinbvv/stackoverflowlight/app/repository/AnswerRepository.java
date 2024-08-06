package rockinbvv.stackoverflowlight.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rockinbvv.stackoverflowlight.app.model.Answer;
import rockinbvv.stackoverflowlight.app.model.Post;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {


    List<Answer> findAnswersByPost(Post post);//todo change to postId

}
