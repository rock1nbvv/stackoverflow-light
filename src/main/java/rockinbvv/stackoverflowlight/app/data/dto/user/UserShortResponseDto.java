package rockinbvv.stackoverflowlight.app.data.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserShortResponseDto {
    private String name;
    private String email;
}
