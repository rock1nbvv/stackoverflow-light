package rockinbvv.stackoverflowlight.app.repository;

import org.springframework.stereotype.Repository;
import rockinbvv.stackoverflowlight.app.data.model.Answer;

import java.util.Optional;

@Repository
public class AnswerRepositoryImpl implements AnswerRepositoryCustom {

    @Override
    public Optional<Answer> someCustomMethod(Answer answer) {
        return Optional.of(Answer.builder().build());
        // Your custom implementation
    }
}
