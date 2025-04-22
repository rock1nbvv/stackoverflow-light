package rockinbvv.stackoverflowlight.app.data.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthDto {
    private Long id;
    private Long userId;
    private String authType;
    private String password;
    private String googleId;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}