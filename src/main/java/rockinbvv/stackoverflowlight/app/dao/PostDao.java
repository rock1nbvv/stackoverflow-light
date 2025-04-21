package rockinbvv.stackoverflowlight.app.dao;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;
import rockinbvv.stackoverflowlight.app.data.post.CreatePostRequest;
import rockinbvv.stackoverflowlight.app.data.post.PostResponseDto;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PostDao {
    private final JdbcClient jdbc;

    public long create(CreatePostRequest dto) {
        return jdbc
                .sql("""
                        INSERT INTO post (title, body, id_author, creation_date)
                        VALUES (:title, :body, :authorId, :now)
                        RETURNING id
                        """)
                .param("title", dto.getTitle())
                .param("body", dto.getBody())
                .param("authorId", dto.getAuthorId())
                .param("now", Instant.now())
                .query(Long.class)
                .single();
    }

    public Optional<PostResponseDto> findById(long id) {
        return jdbc
                .sql("""
                        SELECT id, title, body, id_author, creation_date, upvote_count, downvote_count
                        FROM post
                        WHERE id = :id
                        """)
                .param("id", id)
                .query(PostResponseDto.class)
                .optional();
    }

    public List<PostResponseDto> findPaginated(int page, int size) {
        return jdbc
                .sql("""
                        SELECT id, title, body, id_author, creation_date, upvote_count, downvote_count
                        FROM post
                        ORDER BY creation_date DESC
                        OFFSET :offset LIMIT :limit
                        """)
                .param("offset", page * size)
                .param("limit", size)
                .query(PostResponseDto.class)
                .list();
    }
    public long countAll() {
        return jdbc.sql("SELECT COUNT(*) FROM post")
                .query(Long.class)
                .single();
    }
}
