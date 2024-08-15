package rockinbvv.stackoverflowlight.app.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rockinbvv.stackoverflowlight.app.data.dto.answer.request.AnswerCreateDto;
import rockinbvv.stackoverflowlight.app.data.dto.answer.response.FullAnswerResponseDto;
import rockinbvv.stackoverflowlight.app.data.model.Answer;
import rockinbvv.stackoverflowlight.app.data.model.Post;
import rockinbvv.stackoverflowlight.app.data.model.User;
import rockinbvv.stackoverflowlight.app.repository.AnswerRepository;
import rockinbvv.stackoverflowlight.app.repository.PostRepository;
import rockinbvv.stackoverflowlight.app.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public Answer getVladById() {
        Optional<Answer> test = answerRepository.someCustomMethod(Answer.builder().build());

        return test.orElse(null);
    }

    @Transactional
    public FullAnswerResponseDto getAnswerById(Long id) {
        FullAnswerResponseDto answer = answerRepository.findAnswerById(id).orElse(null);

        return answer;
    }

    @Transactional
    public Answer saveAnswer(AnswerCreateDto answerCreateDto) {
        User user = userRepository.findById(answerCreateDto.getIdAuthor())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Post post = postRepository.findById(answerCreateDto.getIdPost())
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        Answer.AnswerBuilder answerBuilder = Answer.builder()
                .author(user)
                .post(post)
                .body(answerCreateDto.getBody());

        if (answerCreateDto.getIdParent() != null) {
            Answer parent = answerRepository.findById(answerCreateDto.getIdParent())
                    .orElseThrow(() -> new EntityNotFoundException("Parent Answer not found"));

            answerBuilder.parent(parent);
        }

        return answerRepository.save(answerBuilder.build());
    }

    public void deleteAnswer(Long id) {
        answerRepository.deleteById(id);
    }

}
