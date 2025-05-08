package rockinbvv.stackoverflowlight.app.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rockinbvv.stackoverflowlight.app.data.post.CreatePostRequest;
import rockinbvv.stackoverflowlight.app.data.post.PostResponseDto;
import rockinbvv.stackoverflowlight.app.data.vote.RequestVoteDto;
import rockinbvv.stackoverflowlight.app.service.AnswerService;
import rockinbvv.stackoverflowlight.app.service.PostService;
import rockinbvv.stackoverflowlight.system.PaginatedResponse;
import rockinbvv.stackoverflowlight.system.ResponseWrapper;
import rockinbvv.stackoverflowlight.system.security.CurrentUserProvider;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
@Tag(name = "Post")
@PreAuthorize("hasAuthority('OIDC_USER') or hasAuthority('LOCAL_USER')")
public class PostController {

    private final CurrentUserProvider currentUserProvider;
    private final PostService postService;
    private final AnswerService answerService;

    @PostMapping(consumes = "application/json")
    public ResponseWrapper<Long> create(@RequestBody CreatePostRequest createPostRequest) {
        Long userId = currentUserProvider.get().userId();
        return ResponseWrapper.ok(postService.create(createPostRequest, userId));
    }

    @GetMapping("/{id}")
    public ResponseWrapper<PostResponseDto> getById(@PathVariable Long id) {
        return ResponseWrapper.ok(postService.getById(id));
    }

    @GetMapping
    public ResponseWrapper<PaginatedResponse<PostResponseDto>> getPaginatedPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseWrapper.ok(postService.getPaginatedPosts(page, size));
    }

    @PostMapping("/{postId}/vote")
    public void submitPostVote(@PathVariable long postId, @RequestBody RequestVoteDto requestVoteDto) {
        Long userId = currentUserProvider.get().userId();
        postService.submitVote(userId, postId, requestVoteDto);
    }
}
