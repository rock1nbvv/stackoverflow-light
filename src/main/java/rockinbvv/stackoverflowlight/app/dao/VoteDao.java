package rockinbvv.stackoverflowlight.app.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import rockinbvv.stackoverflowlight.app.data.vote.RequestVoteDto;
import rockinbvv.stackoverflowlight.app.data.vote.VoteStats;

import java.time.Instant;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class VoteDao {
    private final JdbcClient jdbc;

    public void submitVote(long userId, RequestVoteDto dto) {
        jdbc.sql("""
                        INSERT INTO vote (id_user, id_post, id_answer, upvote, created_at)
                        VALUES (:userId, :postId, :answerId, :upvote, :createdAt)
                        ON CONFLICT (id_user, id_post, id_answer)
                        DO UPDATE SET upvote = EXCLUDED.upvote, created_at = EXCLUDED.created_at
                        """)
                .param("userId", userId)
                .param("postId", dto.getPostId())
                .param("answerId", dto.getAnswerId())
                .param("upvote", dto.getUpvote())
                .param("createdAt", Instant.now())
                .update();
    }

    public VoteStats computeStatsForPost(long postId) {
        return jdbc.sql("""
                            SELECT
                                COUNT(*) FILTER (WHERE upvote) AS upvotes,
                                COUNT(*) FILTER (WHERE NOT upvote) AS downvotes
                            FROM vote
                            WHERE id_post = :postId
                        """)
                .param("postId", postId)
                .query(VoteStats.class)
                .single();
    }

    public VoteStats computeStatsForAnswer(long answerId) {
        return jdbc.sql("""
                            SELECT
                                COUNT(*) FILTER (WHERE upvote) AS upvotes,
                                COUNT(*) FILTER (WHERE NOT upvote) AS downvotes
                            FROM vote
                            WHERE id_answer = :answerId
                        """)
                .param("answerId", answerId)
                .query(VoteStats.class)
                .single();
    }

    public List<Long> findAllPostIdsWithVotes() {
        return jdbc.sql("""
                    SELECT DISTINCT id_post FROM vote
                    WHERE id_post IS NOT NULL
                """).query(Long.class).list();
    }

    public List<Long> findAllAnswerIdsWithVotes() {
        return jdbc.sql("""
                    SELECT DISTINCT id_answer FROM vote
                    WHERE id_answer IS NOT NULL
                """).query(Long.class).list();
    }
}
