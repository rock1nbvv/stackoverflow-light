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
                            INSERT INTO app_user (name, email, password, google_id)
                            VALUES (:name, :email, :password, :googleId)
                            RETURNING id
                        """)
                .param("name", userCreateDto.getName())
                .param("email", userCreateDto.getEmail())
                .param("password", userCreateDto.getPassword())
                .param("googleId", userCreateDto.getGoogleId())
                .query(Long.class)
                .single();
    }

    public Optional<UserResponseDto> findByEmail(String email) {
        return jdbcClient.sql("""
                        SELECT
                        u,id,
                        u.name,
                        u.email
                        FROM app_user u
                        WHERE email = :email
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
                        u.google_id
                        FROM app_user u
                        WHERE email = :email
                        """)
                .param("email", email)
                .query(new OidcUserMapper())
                .optional();
    }

    public Optional<UserResponseDto> findById(Long id) {
        return jdbcClient.sql("""
                        SELECT
                        u,id,
                        u.name,
                        u.email
                        FROM app_user u
                        WHERE id = :id
                        """)
                .param("id", id)
                .query(new UserResponseDtoRowMapper())
                .optional();
    }

    public Optional<UserFullResponseDto> findFullUserById(Long id) {
        return jdbcClient.sql("""
                        SELECT
                        u,id,
                        u.name,
                        u.password,
                        u.google_id,
                        u.email
                        FROM app_user u
                        WHERE id = :id
                        """)
                .param("id", id)
                .query(new UserFullResponseDtoRowMapper())
                .optional();
    }

    public void corruptById(Long userId) {
        jdbcClient.sql("""
                        UPDATE app_user
                        SET name = 'wiped',
                            email = 'wiped',
                            password = NULL,
                            google_id = NULL
                        WHERE id = :id
                        """)
                .param("id", userId)
                .update();
    }
}
