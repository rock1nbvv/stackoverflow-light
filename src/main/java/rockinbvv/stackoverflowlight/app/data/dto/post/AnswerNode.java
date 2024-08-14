package rockinbvv.stackoverflowlight.app.data.dto.post;

import lombok.Builder;
import lombok.Data;
import rockinbvv.stackoverflowlight.app.data.dto.answer.AnswerResponseDto;

import java.util.List;

@Data
@Builder
public class AnswerNode {
    private Long id;
    private AnswerResponseDto answer;
    List<AnswerNode> children;
}
