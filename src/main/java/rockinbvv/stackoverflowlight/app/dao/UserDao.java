package rockinbvv.stackoverflowlight.app.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;
import rockinbvv.stackoverflowlight.app.data.user.OidcUserMapper;
import rockinbvv.stackoverflowlight.app.data.user.OidcUserResponseDto;
import rockinbvv.stackoverflowlight.app.data.user.UserCreateDto;
import rockinbvv.stackoverflowlight.app.data.user.UserFullResponseDto;
import rockinbvv.stackoverflowlight.app.data.user.UserFullResponseDtoRowMapper;
import rockinbvv.stackoverflowlight.app.data.user.UserResponseDto;
import rockinbvv.stackoverflowlight.app.data.user.UserResponseDtoRowMapper;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDao {

    private final JdbcClient jdbcClient;

    public Long register(UserCreateDto userCreateDto) {
        return jdbcClient.sql("""
                            INSERT INTO app_user (name, email, is_admin)
                            VALUES (:name, :email, :isAdmin)
                            RETURNING id
                        """)
                .param("name", userCreateDto.getName())
                .param("email", userCreateDto.getEmail())
                .param("isAdmin", userCreateDto.getIsAdmin() != null ? userCreateDto.getIsAdmin() : false)
                .query(Long.class)
                .single();
    }

    public Optional<UserResponseDto> findByEmail(String email) {
        return jdbcClient.sql("""
                        SELECT
                        u.id,
                        u.name,
                        u.email,
                        u.is_admin
                        FROM app_user u
                        WHERE u.email = :email
                        """)
                .param("email", email)
                .query(new UserResponseDtoRowMapper())
                .optional();
    }

    public Optional<OidcUserResponseDto> findOidcUserByEmail(String email) {
        return jdbcClient.sql("""
                        SELECT
                        u.id,
                        u.name,
                        u.email,
                        ua.google_id,
                        u.is_admin
                        FROM app_user u
                        LEFT JOIN user_auth ua ON u.id = ua.id_user AND ua.auth_type = 'GOOGLE'
                        WHERE u.email = :email
                        """)
                .param("email", email)
                .query(new OidcUserMapper())
                .optional();
    }

    public Optional<UserFullResponseDto> findFullUserByEmail(String email) {
        return jdbcClient.sql("""
                        SELECT
                        u.id,
                        u.name,
                        u.email,
                        u.is_admin,
                        ua_pass.password,
                        ua_google.google_id
                        FROM app_user u
                        LEFT JOIN user_auth ua_pass ON u.id = ua_pass.id_user AND ua_pass.auth_type = 'PASSWORD'
                        LEFT JOIN user_auth ua_google ON u.id = ua_google.id_user AND ua_google.auth_type = 'GOOGLE'
                        WHERE u.email = :email
                        """)
                .param("email", email)
                .query(new UserFullResponseDtoRowMapper())
                .optional();
    }

    public Optional<UserResponseDto> findById(Long id) {
        return jdbcClient.sql("""
                        SELECT
                        u.id,
                        u.name,
                        u.email,
                        u.is_admin
                        FROM app_user u
                        WHERE u.id = :id
                        """)
                .param("id", id)
                .query(new UserResponseDtoRowMapper())
                .optional();
    }

    public Optional<UserFullResponseDto> findFullUserById(Long id) {
        return jdbcClient.sql("""
                        SELECT
                        u.id,
                        u.name,
                        u.email,
                        u.is_admin,
                        ua_pass.password,
                        ua_google.google_id
                        FROM app_user u
                        LEFT JOIN user_auth ua_pass ON u.id = ua_pass.id_user AND ua_pass.auth_type = 'PASSWORD'
                        LEFT JOIN user_auth ua_google ON u.id = ua_google.id_user AND ua_google.auth_type = 'GOOGLE'
                        WHERE u.id = :id
                        """)
                .param("id", id)
                .query(new UserFullResponseDtoRowMapper())
                .optional();
    }

    public void corruptById(Long userId) {
        // Delete all auth records for this user
        jdbcClient.sql("""
                        DELETE FROM user_auth
                        WHERE id_user = :id
                        """)
                .param("id", userId)
                .update();

        // Update user record
        jdbcClient.sql("""
                        UPDATE app_user
                        SET name = 'wiped',
                            email = 'wiped'
                        WHERE id = :id
                        """)
                .param("id", userId)
                .update();
    }
}
