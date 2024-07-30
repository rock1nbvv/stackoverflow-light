package rockinbvv.stackoverflowlight.app.dto.post;

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
