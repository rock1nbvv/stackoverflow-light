package rockinbvv.stackoverflowlight.app.data.dto.user.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserFullResponseDto {
    private Long id;
    private String name;
    private String password;
    private String email;
}
