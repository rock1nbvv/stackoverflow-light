package rockinbvv.stackoverflowlight.app.data.user;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OidcUserMapper implements RowMapper<OidcUserResponseDto> {
    @Override
    public OidcUserResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return OidcUserResponseDto.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .googleId(rs.getString("google_id"))
                .email(rs.getString("email"))
                .build();
    }
}
