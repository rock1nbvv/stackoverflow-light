package rockinbvv.stackoverflowlight.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rockinbvv.stackoverflowlight.app.dao.AnswerDao;
import rockinbvv.stackoverflowlight.app.dao.PostDao;
import rockinbvv.stackoverflowlight.app.data.answer.AnswerCreateDto;
import rockinbvv.stackoverflowlight.app.data.answer.AnswerResponseDto;
import rockinbvv.stackoverflowlight.app.data.vote.RequestVoteDto;
import rockinbvv.stackoverflowlight.app.exception.EntityNotFoundException;
import rockinbvv.stackoverflowlight.app.exception.EntityType;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerDao answerDao;
    private final PostDao postDao;

    @Transactional
    public long create(AnswerCreateDto dto, long authorId) {
        if (postDao.findById(dto.getParentPostId()).isEmpty()) {
            throw new EntityNotFoundException(EntityType.ANSWER, "id", dto.getParentPostId());
        }

        if (dto.getParentAnswerId() != null) {
            AnswerResponseDto parentAnswer = answerDao.findById(dto.getParentAnswerId())
                    .orElseThrow(() -> new EntityNotFoundException(EntityType.ANSWER, "id", dto.getParentAnswerId()));

            if (!parentAnswer.getParentPostId().equals(dto.getParentPostId())) {
                throw new IllegalArgumentException("Parent answer must belong to the same post");
            }
        }
        return answerDao.create(dto, authorId);
    }

    @Transactional(readOnly = true)
    public void submitVote(Long userId, Long answerId, RequestVoteDto requestVoteDto) {
        if (answerDao.findById(answerId).isEmpty()) {
            throw new EntityNotFoundException(EntityType.ANSWER, "id", answerId);
        }
        answerDao.submitVote(userId, answerId, requestVoteDto);
    }
}
