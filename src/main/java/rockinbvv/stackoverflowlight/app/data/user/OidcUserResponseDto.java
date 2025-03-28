package rockinbvv.stackoverflowlight.app.data.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class OidcUserResponseDto {
    long id;
    private String name;
    private String googleId;
    private String email;
}
