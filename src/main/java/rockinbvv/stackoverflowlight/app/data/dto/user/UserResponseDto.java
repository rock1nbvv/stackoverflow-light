package rockinbvv.stackoverflowlight.app.data.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@AllArgsConstructor
//@Builder
//@Jacksonized
public class UserResponseDto {
    private String name;
    private String password;
    private String email;
}
