package rockinbvv.stackoverflowlight.app.data.user;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserResponseDtoRowMapper implements RowMapper<UserResponseDto> {
    @Override
    public UserResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return UserResponseDto.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .email(rs.getString("email"))
                .isAdmin(rs.getBoolean("is_admin"))
                .build();
    }
}
