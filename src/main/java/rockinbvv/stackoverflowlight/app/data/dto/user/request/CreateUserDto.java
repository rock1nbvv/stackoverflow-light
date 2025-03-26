package rockinbvv.stackoverflowlight.app.data.dto.user.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CreateUserDto {
    private String name;
    private String password;
    private String googleId;
    private String email;
}
