package rockinbvv.stackoverflowlight.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rockinbvv.stackoverflowlight.app.dao.AnswerDao;
import rockinbvv.stackoverflowlight.app.dao.PostDao;
import rockinbvv.stackoverflowlight.app.dao.VoteDao;
import rockinbvv.stackoverflowlight.app.data.vote.RequestVoteDto;
import rockinbvv.stackoverflowlight.app.exception.EntityNotFoundException;
import rockinbvv.stackoverflowlight.app.exception.EntityType;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteDao voteDao;
    private final PostDao postDao;
    private final AnswerDao answerDao;

    @Transactional
    public void submitVote(RequestVoteDto dto, long userId) {
        if (dto.getPostId() == null && dto.getAnswerId() == null) {
            throw new IllegalArgumentException("Either postId or answerId must be provided");
        }

        if (dto.getPostId() != null && postDao.findById(dto.getPostId()).isEmpty()) {
            throw new EntityNotFoundException(EntityType.POST, "id", dto.getPostId());
        }

        if (dto.getAnswerId() != null && answerDao.findById(dto.getAnswerId()).isEmpty()) {
            throw new EntityNotFoundException(EntityType.ANSWER, "id", dto.getAnswerId());
        }

        voteDao.submitVote(userId, dto);
    }
}
