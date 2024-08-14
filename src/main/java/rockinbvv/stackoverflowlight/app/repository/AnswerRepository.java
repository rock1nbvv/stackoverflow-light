package rockinbvv.stackoverflowlight.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rockinbvv.stackoverflowlight.app.data.dto.answer.response.FullAnswerResponseDto;
import rockinbvv.stackoverflowlight.app.data.model.Answer;
import rockinbvv.stackoverflowlight.app.data.model.Post;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long>, AnswerRepositoryCustom {

    Optional<FullAnswerResponseDto> findAnswerById(Long id);

    List<Answer> findAnswersByPost(Post post); // todo change to postId
}
