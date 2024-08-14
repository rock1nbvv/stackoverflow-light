package rockinbvv.stackoverflowlight.app.repository;

import rockinbvv.stackoverflowlight.app.data.model.Answer;

import java.util.Optional;

public interface AnswerRepositoryCustom {
    Optional<Answer> someCustomMethod(Answer answer);
}
