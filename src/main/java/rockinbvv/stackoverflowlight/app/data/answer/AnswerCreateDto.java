package rockinbvv.stackoverflowlight.app.data.answer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AnswerCreateDto {
    @NotBlank
    private String body;
    @NotNull
    private Long parentPostId;
    private Long parentAnswerId;
}
