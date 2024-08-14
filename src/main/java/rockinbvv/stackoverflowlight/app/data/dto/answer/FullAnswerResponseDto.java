package rockinbvv.stackoverflowlight.app.data.dto.answer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class FullAnswerResponseDto {
    private Long id;
    private String body;
}
