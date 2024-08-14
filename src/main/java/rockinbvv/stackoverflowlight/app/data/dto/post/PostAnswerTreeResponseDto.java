package rockinbvv.stackoverflowlight.app.data.dto.post;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PostAnswerTreeResponseDto {
    private PostResponseDto post;
    private List<AnswerNode> answers;
}
