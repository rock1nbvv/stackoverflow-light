package rockinbvv.stackoverflowlight.app.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;
import rockinbvv.stackoverflowlight.app.data.user.UserAuthDto;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserAuthDao {

    private final JdbcClient jdbcClient;

    public Long createPasswordAuth(Long userId, String password) {
        return jdbcClient.sql("""
                INSERT INTO user_auth (id_user, auth_type, password)
                VALUES (:userId, 'PASSWORD', :password)
                RETURNING id
                """)
                .param("userId", userId)
                .param("password", password)
                .query(Long.class)
                .single();
    }

    public Long createGoogleAuth(Long userId, String googleId) {
        return jdbcClient.sql("""
                INSERT INTO user_auth (id_user, auth_type, google_id)
                VALUES (:userId, 'GOOGLE', :googleId)
                RETURNING id
                """)
                .param("userId", userId)
                .param("googleId", googleId)
                .query(Long.class)
                .single();
    }

    public Optional<UserAuthDto> findPasswordAuthByUserId(Long userId) {
        return jdbcClient.sql("""
                SELECT
                ua.id,
                ua.id_user,
                ua.auth_type,
                ua.password
                FROM user_auth ua
                WHERE ua.id_user = :userId AND ua.auth_type = 'PASSWORD'
                """)
                .param("userId", userId)
                .query((rs, rowNum) -> UserAuthDto.builder()
                        .id(rs.getLong("id"))
                        .userId(rs.getLong("id_user"))
                        .authType(rs.getString("auth_type"))
                        .password(rs.getString("password"))
                        .build())
                .optional();
    }

    public Optional<UserAuthDto> findGoogleAuthByUserId(Long userId) {
        return jdbcClient.sql("""
                SELECT
                ua.id,
                ua.id_user,
                ua.auth_type,
                ua.google_id
                FROM user_auth ua
                WHERE ua.id_user = :userId AND ua.auth_type = 'GOOGLE'
                """)
                .param("userId", userId)
                .query((rs, rowNum) -> UserAuthDto.builder()
                        .id(rs.getLong("id"))
                        .userId(rs.getLong("id_user"))
                        .authType(rs.getString("auth_type"))
                        .googleId(rs.getString("google_id"))
                        .build())
                .optional();
    }

    public Optional<UserAuthDto> findGoogleAuthByGoogleId(String googleId) {
        return jdbcClient.sql("""
                SELECT
                ua.id,
                ua.id_user,
                ua.auth_type,
                ua.google_id
                FROM user_auth ua
                WHERE ua.google_id = :googleId
                """)
                .param("googleId", googleId)
                .query((rs, rowNum) -> UserAuthDto.builder()
                        .id(rs.getLong("id"))
                        .userId(rs.getLong("id_user"))
                        .authType(rs.getString("auth_type"))
                        .googleId(rs.getString("google_id"))
                        .build())
                .optional();
    }

    public List<UserAuthDto> findAllAuthsByUserId(Long userId) {
        return jdbcClient.sql("""
                SELECT
                ua.id,
                ua.id_user,
                ua.auth_type,
                ua.password,
                ua.google_id
                FROM user_auth ua
                WHERE ua.id_user = :userId
                """)
                .param("userId", userId)
                .query((rs, rowNum) -> {
                    UserAuthDto.UserAuthDtoBuilder builder = UserAuthDto.builder()
                            .id(rs.getLong("id"))
                            .userId(rs.getLong("id_user"))
                            .authType(rs.getString("auth_type"));
                    
                    if ("PASSWORD".equals(rs.getString("auth_type"))) {
                        builder.password(rs.getString("password"));
                    } else if ("GOOGLE".equals(rs.getString("auth_type"))) {
                        builder.googleId(rs.getString("google_id"));
                    }
                    
                    return builder.build();
                })
                .list();
    }

    public void updatePassword(Long userId, String password) {
        jdbcClient.sql("""
                UPDATE user_auth
                SET password = :password,
                    updated_at = NOW()
                WHERE id_user = :userId AND auth_type = 'PASSWORD'
                """)
                .param("userId", userId)
                .param("password", password)
                .update();
    }

    public void deleteAuthsByUserId(Long userId) {
        jdbcClient.sql("""
                DELETE FROM user_auth
                WHERE id_user = :userId
                """)
                .param("userId", userId)
                .update();
    }
}