package rockinbvv.stackoverflowlight.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rockinbvv.stackoverflowlight.app.data.vote.RequestVoteDto;
import rockinbvv.stackoverflowlight.app.service.VoteService;
import rockinbvv.stackoverflowlight.system.ResponseWrapper;
import rockinbvv.stackoverflowlight.system.security.CurrentUserProvider;

@RestController
@RequestMapping("/vote")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('OIDC_USER') or hasAuthority('LOCAL_USER')")
public class VoteController {

    private final VoteService voteService;
    private final CurrentUserProvider currentUserProvider;

    @PostMapping(consumes = "application/json")
    public ResponseWrapper submitVote(@RequestBody @Valid RequestVoteDto dto) {
        long userId = currentUserProvider.get().userId();
        voteService.submitVote(dto, userId);
        return ResponseWrapper.ok();
    }
}
