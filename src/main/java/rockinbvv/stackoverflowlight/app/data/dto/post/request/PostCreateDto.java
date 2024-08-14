package rockinbvv.stackoverflowlight.app.data.dto.post.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostCreateDto {
    private String title;
    private String body;
    @NotNull
    private Long idAuthor;
}
