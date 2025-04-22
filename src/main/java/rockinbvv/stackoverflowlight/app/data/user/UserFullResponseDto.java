package rockinbvv.stackoverflowlight.app.data.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserFullResponseDto {
    private Long id;
    private String name;
    private String password;
    private String googleId;
    private String email;
    private Boolean isAdmin;
}
