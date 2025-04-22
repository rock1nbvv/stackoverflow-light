package rockinbvv.stackoverflowlight.app.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;
import rockinbvv.stackoverflowlight.app.data.answer.AnswerCreateDto;
import rockinbvv.stackoverflowlight.app.data.answer.AnswerResponseDto;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AnswerDao {
    private final JdbcClient jdbc;

    public long create(AnswerCreateDto dto, long authorId) {
        return jdbc
                .sql("""
                        INSERT INTO answer (body, id_author, id_post, id_parent, creation_date)
                        VALUES (:body, :authorId, :postId, :parentId, :now)
                        RETURNING id
                        """)
                .param("body", dto.getBody())
                .param("authorId", authorId)
                .param("postId", dto.getPostId())
                .param("parentId", dto.getParentId())
                .param("now", Instant.now())
                .query(Long.class)
                .single();
    }

    public Optional<AnswerResponseDto> findById(long id) {
        return jdbc.sql("""
                        SELECT id, body, id_author, id_post, id_parent, creation_date, upvote_count, downvote_count
                        FROM answer
                        WHERE id = :id
                        """)
                .param("id", id)
                .query(AnswerResponseDto.class)
                .optional();
    }

    public List<AnswerResponseDto> findByPostId(long postId) {
        return jdbc
                .sql("""
                        SELECT id, body, id_author, id_post, id_parent, creation_date, upvote_count, downvote_count
                        FROM answer
                        WHERE id_post = :postId
                        ORDER BY creation_date
                        """)
                .param("postId", postId)
                .query(AnswerResponseDto.class)
                .list();
    }

    public void updateVoteStats(long answerId, int upvotes, int downvotes) {
        jdbc.sql("""
                        UPDATE answer
                        SET upvote_count = :upvotes,
                            downvote_count = :downvotes
                        WHERE id = :answerId
                        """)
                .param("upvotes", upvotes)
                .param("downvotes", downvotes)
                .param("answerId", answerId)
                .update();
    }

}
