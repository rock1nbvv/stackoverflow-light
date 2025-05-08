package rockinbvv.stackoverflowlight.app.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;
import rockinbvv.stackoverflowlight.app.data.answer.AnswerCreateDto;
import rockinbvv.stackoverflowlight.app.data.answer.AnswerResponseDto;
import rockinbvv.stackoverflowlight.app.data.vote.RequestVoteDto;

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
                        INSERT INTO answer (body, id_author, id_parent_post, id_parent_answer, creation_date)
                        VALUES (:body, :authorId, :postId, :parentId, :now)
                        RETURNING id
                        """)
                .param("body", dto.getBody())
                .param("authorId", authorId)
                .param("postId", dto.getParentPostId())
                .param("parentId", dto.getParentAnswerId())
                .param("now", Instant.now())
                .query(Long.class)
                .single();
    }

    public Optional<AnswerResponseDto> findById(long id) {
        return jdbc.sql("""
                        SELECT id, body, id_author, id_parent_post, id_parent_answer, creation_date
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
                        SELECT id, body, id_author, id_parent_post, id_parent_answer, creation_date
                        FROM answer
                        WHERE id_parent_post = :postId
                        ORDER BY creation_date
                        """)
                .param("postId", postId)
                .query(AnswerResponseDto.class)
                .list();
    }

    public void submitVote(Long authorId, Long answerId, RequestVoteDto dto) {
        jdbc.sql("""
                        INSERT INTO post_vote (id_user, id_post, upvote)
                        VALUES (:userId, :answerId, :upvote)
                        """)
                .param("userId", authorId)
                .param("answerId", answerId)
                .param("upvote", dto.getUpvote())
                .update();
    }
}
