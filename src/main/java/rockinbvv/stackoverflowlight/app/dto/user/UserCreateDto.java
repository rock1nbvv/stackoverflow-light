package rockinbvv.stackoverflowlight.app.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCreateDto {
    private String name;
    private String password;
    private String email;
}
