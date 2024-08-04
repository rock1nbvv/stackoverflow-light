package rockinbvv.stackoverflowlight.app.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rockinbvv.stackoverflowlight.app.dto.answer.AnswerResponseDto;
import rockinbvv.stackoverflowlight.app.dto.post.PostCreateDto;
import rockinbvv.stackoverflowlight.app.dto.post.PostResponseDto;
import rockinbvv.stackoverflowlight.app.model.Answer;
import rockinbvv.stackoverflowlight.app.model.Post;
import rockinbvv.stackoverflowlight.app.model.User;
import rockinbvv.stackoverflowlight.app.repository.AnswerRepository;
import rockinbvv.stackoverflowlight.app.repository.PostRepository;
import rockinbvv.stackoverflowlight.app.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Transactional
    public PostAnswerTree getPostAnswerTree(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        List<Answer> postAnswers = answerRepository.findAnswersByPost(post);

        Map<Long, List<AnswerResponseDto>> childAnswerMap = postAnswers.stream()
                .filter(answer -> answer.getParent() != null)
                .map(answer -> AnswerResponseDto.builder()
                        .id(answer.getId())
                        .body(answer.getBody())
                        .authorId(answer.getAuthor().getId())
                        .parentId(answer.getParent().getId())
                        .postId(answer.getPost().getId())
                        .build())
                .collect(Collectors.groupingBy(AnswerResponseDto::getAuthorId));

        PostAnswerTree postAnswerTree = PostAnswerTree.builder()
                .post(PostResponseDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .body(post.getBody())
                        .idAuthor(post.getAuthor().getId())
                        .build())
                .postAnswers(
                        postAnswers.stream()
                                .filter(answer -> answer.getParent() == null)
                                .map(answer ->
                                        PostAnswer.builder()
                                                .answer(AnswerResponseDto.builder()
                                                        .id(answer.getId())
                                                        .body(answer.getBody())
                                                        .postId(answer.getPost().getId())
                                                        .authorId(answer.getAuthor().getId())
                                                        .build())
                                                .childAnswers(childAnswerMap.get(answer.getId()))
                                                .build()
                                )
                                .toList())
                .build();
        return postAnswerTree;
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PostAnswerTree {
        private PostResponseDto post;
        private List<PostAnswer> postAnswers;
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PostAnswer {
        private AnswerResponseDto answer;
        private List<AnswerResponseDto> childAnswers;
    }
}
