package rockinbvv.stackoverflowlight.app.data.post;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String body;

    private long authorId;
}
