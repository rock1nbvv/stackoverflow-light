package rockinbvv.stackoverflowlight.app.data.vote;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestVoteDto {
    @NotNull
    private Boolean upvote;
}
