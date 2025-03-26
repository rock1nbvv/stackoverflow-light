package rockinbvv.stackoverflowlight.app.data;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;

    @NotNull
    private String name;
    private String password;
    private String googleId;

    @NotNull
    private String email;
}
