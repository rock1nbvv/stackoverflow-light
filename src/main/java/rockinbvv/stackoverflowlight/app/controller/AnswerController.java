package rockinbvv.stackoverflowlight.app.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rockinbvv.stackoverflowlight.app.data.answer.AnswerCreateDto;
import rockinbvv.stackoverflowlight.app.data.vote.RequestVoteDto;
import rockinbvv.stackoverflowlight.app.service.AnswerService;
import rockinbvv.stackoverflowlight.system.ResponseWrapper;
import rockinbvv.stackoverflowlight.system.security.CurrentUserProvider;

@RestController
@RequestMapping("/api/answer")
@RequiredArgsConstructor
@Tag(name = "Answer")
@PreAuthorize("hasAuthority('OIDC_USER') or hasAuthority('LOCAL_USER')")
public class AnswerController {

    private final CurrentUserProvider currentUserProvider;
    private final AnswerService answerService;

    @PostMapping(consumes = "application/json")
    public ResponseWrapper<Long> create(@RequestBody @Valid AnswerCreateDto dto) {
        long userId = currentUserProvider.get().userId();
        return ResponseWrapper.ok(answerService.create(dto, userId));
    }

    @PostMapping("/{answerId}/vote")
    public void submitPostVote(@PathVariable long answerId, @RequestBody RequestVoteDto requestVoteDto) {
        Long userId = currentUserProvider.get().userId();
        answerService.submitVote(userId, answerId, requestVoteDto);
    }
}
