package rockinbvv.stackoverflowlight.app.data.dto.post.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostCreateDto {
    private String title;
    private String body;
}
