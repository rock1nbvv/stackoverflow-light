package rockinbvv.stackoverflowlight.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rockinbvv.stackoverflowlight.app.dao.AnswerDao;
import rockinbvv.stackoverflowlight.app.dao.PostDao;
import rockinbvv.stackoverflowlight.app.dao.VoteDao;
import rockinbvv.stackoverflowlight.app.data.vote.VoteStats;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoteAggregationService {

    private final VoteDao voteDao;
    private final PostDao postDao;
    private final AnswerDao answerDao;

    @Scheduled(fixedDelay = 10000) // run every 10 seconds
    public void aggregateAllVotes() {
        aggregatePostVotes();
        aggregateAnswerVotes();
    }

    private void aggregatePostVotes() {
        List<Long> postIds = voteDao.findAllPostIdsWithVotes();
        for (Long postId : postIds) {
            VoteStats stats = voteDao.computeStatsForPost(postId);
            postDao.updateVoteStats(postId, stats.getUpvotes(), stats.getDownvotes());
        }
    }

    private void aggregateAnswerVotes() {
        List<Long> answerIds = voteDao.findAllAnswerIdsWithVotes();
        for (Long answerId : answerIds) {
            VoteStats stats = voteDao.computeStatsForAnswer(answerId);
            answerDao.updateVoteStats(answerId, stats.getUpvotes(), stats.getDownvotes());
        }
    }
}
