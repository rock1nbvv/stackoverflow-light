package rockinbvv.stackoverflowlight.app.data.dto.answer;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FullAnswerResponseDto {
    private Long id;
    private String body;
}
