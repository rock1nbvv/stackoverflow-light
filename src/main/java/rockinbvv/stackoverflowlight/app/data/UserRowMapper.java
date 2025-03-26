package rockinbvv.stackoverflowlight.app.data;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .password(rs.getString("password"))
                .googleId(rs.getString("google_id"))
                .email(rs.getString("email"))
                .build();
    }
}
