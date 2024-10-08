package rockinbvv.stackoverflowlight.app.data.dto.user.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCreateDto {
    private String name;
    private String password;
    private String email;
}
