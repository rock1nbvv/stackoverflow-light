package rockinbvv.stackoverflowlight.app.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rockinbvv.stackoverflowlight.app.dto.post.PostCreateDto;
import rockinbvv.stackoverflowlight.app.model.Answer;
import rockinbvv.stackoverflowlight.app.model.Post;
import rockinbvv.stackoverflowlight.app.model.User;
import rockinbvv.stackoverflowlight.app.repository.AnswerRepository;
import rockinbvv.stackoverflowlight.app.repository.PostRepository;
import rockinbvv.stackoverflowlight.app.repository.UserRepository;

import java.util.List;

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
    public Post savePost(PostCreateDto postCreateDto) {
        User user = userRepository.findById(postCreateDto.getIdAuthor())
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

        List<Answer> answers = answerRepository.findAnswersByPost(post);
        return answers;
    }
}
