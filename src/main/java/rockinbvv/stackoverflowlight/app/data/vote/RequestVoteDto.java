package rockinbvv.stackoverflowlight.app.data.vote;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestVoteDto {
    private Long postId;
    private Long answerId;
    @NotNull
    private Boolean upvote;
}
