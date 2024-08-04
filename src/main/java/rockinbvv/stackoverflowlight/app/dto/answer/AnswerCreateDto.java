package rockinbvv.stackoverflowlight.app.dto.answer;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnswerCreateDto {
    private String body;
    private Long idAuthor;
    private Long idPost;
    private Long idParent;
}
