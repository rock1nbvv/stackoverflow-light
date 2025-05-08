package rockinbvv.stackoverflowlight.app.data.answer;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnswerResponseDto {
    private Long id;
    private String body;
    private Long authorId;
    private Long parentPostId;
    private Long parentAnswerId;
    private Instant creationDate;
}
