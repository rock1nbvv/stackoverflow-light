package rockinbvv.stackoverflowlight.app.data.user;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserFullResponseDtoRowMapper implements RowMapper<UserFullResponseDto> {
    @Override
    public UserFullResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return UserFullResponseDto.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .password(rs.getString("password"))
                .googleId(rs.getString("google_id"))
                .email(rs.getString("email"))
                .build();
    }
}
