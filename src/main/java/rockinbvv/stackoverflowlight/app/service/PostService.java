package rockinbvv.stackoverflowlight.app.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rockinbvv.stackoverflowlight.app.data.dto.answer.response.AnswerResponseDto;
import rockinbvv.stackoverflowlight.app.data.dto.post.request.PostCreateDto;
import rockinbvv.stackoverflowlight.app.data.model.Answer;
import rockinbvv.stackoverflowlight.app.data.model.Post;
import rockinbvv.stackoverflowlight.app.data.model.User;
import rockinbvv.stackoverflowlight.app.repository.AnswerRepository;
import rockinbvv.stackoverflowlight.app.repository.PostRepository;
import rockinbvv.stackoverflowlight.app.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;

    public Post getPostById(Long id) {
        return postRepository.findById(id)
            .orElse(null);
    }

    @Transactional
    public Post savePost(PostCreateDto postCreateDto, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Post post = Post.builder()
            .title(postCreateDto.getTitle())
            .body(postCreateDto.getBody())
            .author(user)
            .build();

        return postRepository.save(post);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    @Transactional()
    public List<Answer> getPostAnswers(Long id) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        Stream<AnswerResponseDto> answerResponseDtoStream = answerRepository.findAnswersByPost(post).stream().map(answer -> AnswerResponseDto.builder()
            .id(answer.getId())
            .body(answer.getBody())
            .postId(answer.getPost().getId())
            .authorId(Optional.ofNullable(answer.getAuthor())
                .map(User::getId)
                .orElse(null))
            .parentId(Optional.ofNullable(answer.getParent())
                .map(Answer::getId)
                .orElse(null))
            .build());

        List<Answer> answers = answerRepository.findAnswersByPost(post);
        return answers;
    }
}
