package rockinbvv.stackoverflowlight.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rockinbvv.stackoverflowlight.app.dao.PostDao;
import rockinbvv.stackoverflowlight.app.data.post.CreatePostRequest;
import rockinbvv.stackoverflowlight.app.data.post.PostResponseDto;
import rockinbvv.stackoverflowlight.app.exception.EntityNotFoundException;
import rockinbvv.stackoverflowlight.app.exception.EntityType;
import rockinbvv.stackoverflowlight.app.exception.NoEntitiesFoundException;
import rockinbvv.stackoverflowlight.system.PaginatedResponse;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostDao postDao;

    public PostResponseDto getById(Long id) {
        return postDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.POST, "id", id));
    }

    public Long create(CreatePostRequest createPostRequest, Long userId) {
        postDao.findById(userId)
                .orElseThrow(() -> new NoEntitiesFoundException("No entities of type " + EntityType.POST + "found by userId=" + userId));

        return postDao.create(createPostRequest);
    }

    public PaginatedResponse<PostResponseDto> getPaginatedPosts(int page, int size) {
        return PaginatedResponse.<PostResponseDto>builder()
                .data(postDao.findPaginated(page, size))
                .page(page)
                .size(size)
                .total(postDao.countAll())
                .build();
    }
}
