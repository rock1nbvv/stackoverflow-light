package rockinbvv.stackoverflowlight.app.data.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserCreateDto {
    private String name;
    private String password;
    private String googleId;
    private String email;
}
