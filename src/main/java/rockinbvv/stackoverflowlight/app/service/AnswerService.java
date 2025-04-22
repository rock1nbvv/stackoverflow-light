package rockinbvv.stackoverflowlight.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rockinbvv.stackoverflowlight.app.dao.AnswerDao;
import rockinbvv.stackoverflowlight.app.dao.PostDao;
import rockinbvv.stackoverflowlight.app.dao.VoteDao;
import rockinbvv.stackoverflowlight.app.data.answer.AnswerCreateDto;
import rockinbvv.stackoverflowlight.app.data.answer.AnswerResponseDto;
import rockinbvv.stackoverflowlight.app.data.vote.VoteStats;
import rockinbvv.stackoverflowlight.app.exception.EntityNotFoundException;
import rockinbvv.stackoverflowlight.app.exception.EntityType;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerDao answerDao;
    private final PostDao postDao;
    private final VoteDao voteDao;

    @Transactional
    public long create(AnswerCreateDto dto, long authorId) {
        if (postDao.findById(dto.getPostId()).isEmpty()) {
            throw new EntityNotFoundException(EntityType.ANSWER, "id", dto.getPostId());
        }

        if (dto.getParentId() != null) {
            AnswerResponseDto parentAnswer = answerDao.findById(dto.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException(EntityType.ANSWER, "id", dto.getParentId()));

            if (!parentAnswer.getPostId().equals(dto.getPostId())) {
                throw new IllegalArgumentException("Parent answer must belong to the same post");
            }
        }
        return answerDao.create(dto, authorId);
    }

    @Transactional(readOnly = true)
    public List<AnswerResponseDto> getAnswersForPost(long postId) {
        return answerDao.findByPostId(postId);
    }

    @Transactional(readOnly = true)
    public VoteStats getVoteStats(long answerId) {
        if (answerDao.findById(answerId).isEmpty()) {
            throw new EntityNotFoundException(EntityType.ANSWER, "id", answerId);
        }
        return voteDao.computeStatsForAnswer(answerId);
    }
}
