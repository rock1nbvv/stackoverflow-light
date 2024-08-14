package rockinbvv.stackoverflowlight.app.data.dto.answer;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnswerResponseDto {

    private Long id;

    @NotNull
    private String body;

    private Long authorId;

    private Long postId;

    private Long parentId;
}
