package rockinbvv.stackoverflowlight.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rockinbvv.stackoverflowlight.app.dao.PostDao;
import rockinbvv.stackoverflowlight.app.dao.UserDao;
import rockinbvv.stackoverflowlight.app.dao.VoteDao;
import rockinbvv.stackoverflowlight.app.data.post.CreatePostRequest;
import rockinbvv.stackoverflowlight.app.data.post.PostResponseDto;
import rockinbvv.stackoverflowlight.app.data.vote.VoteStats;
import rockinbvv.stackoverflowlight.app.exception.EntityNotFoundException;
import rockinbvv.stackoverflowlight.app.exception.EntityType;
import rockinbvv.stackoverflowlight.app.exception.NoEntitiesFoundException;
import rockinbvv.stackoverflowlight.system.PaginatedResponse;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostDao postDao;
    private final UserDao userDao;
    private final VoteDao voteDao;

    public PostResponseDto getById(Long id) {
        return postDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.POST, "id", id));
    }

    public Long create(CreatePostRequest createPostRequest, Long userId) {
        userDao.findById(userId)
                .orElseThrow(() -> new NoEntitiesFoundException("No entities of type " + EntityType.USER + " found by userId=" + userId));

        return postDao.create(createPostRequest, userId);
    }

    public PaginatedResponse<PostResponseDto> getPaginatedPosts(int page, int size) {
        return PaginatedResponse.<PostResponseDto>builder()
                .data(postDao.findPaginated(page, size))
                .page(page)
                .size(size)
                .total(postDao.countAll())
                .build();
    }

    @Transactional(readOnly = true)
    public VoteStats getVoteStats(long postId) {
        if (postDao.findById(postId).isEmpty()) {
            throw new EntityNotFoundException(EntityType.POST, "id", postId);
        }
        return voteDao.computeStatsForPost(postId);
    }
}
