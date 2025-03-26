package rockinbvv.stackoverflowlight.app.data;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Answer {

    private Long id;

    @NotNull
    private String body;
    private long authorId;

    @NotNull
    private Post post;
    private Answer parent;
}
