package rockinbvv.stackoverflowlight.app.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;
import rockinbvv.stackoverflowlight.app.data.User;
import rockinbvv.stackoverflowlight.app.data.UserRowMapper;
import rockinbvv.stackoverflowlight.app.data.dto.user.request.CreateUserDto;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDao {

    private final JdbcClient jdbcClient;

    public Long registerUser(CreateUserDto createUserDto) {
        return jdbcClient
                .sql("""
                            INSERT INTO app_user (name, email, password, google_id)
                            VALUES (:name, :email, :password, :googleId)
                            RETURNING id
                        """)
                .param("name", createUserDto.getName())
                .param("email", createUserDto.getEmail())
                .param("password", createUserDto.getPassword())
                .param("googleId", createUserDto.getGoogleId())
                .query(Long.class)
                .single();
    }

    public Optional<User> findOneByEmail(String email) {
        return jdbcClient.sql("""
                        SELECT
                        *
                        FROM app_user
                        WHERE email = :email
                        """)
                .param("email", email)
                .query(new UserRowMapper())
                .optional();
    }

    public Optional<User> findOneById(long id) {
        return jdbcClient.sql("""
                        SELECT
                            *
                        FROM app_user
                        WHERE id = :id
                        """)
                .param("id", id)
                .query(new UserRowMapper())
                .optional();
    }

    public void corruptById(long userId) {
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