//package rockinbvv.stackoverflowlight.app.controller;
//
//
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import rockinbvv.stackoverflowlight.app.data.post.PostCreateDto;
//import rockinbvv.stackoverflowlight.app.data.answer.Answer;
//import rockinbvv.stackoverflowlight.app.data.post.Post;
//import rockinbvv.stackoverflowlight.app.service.PostService;
//import rockinbvv.stackoverflowlight.system.security.CustomOidcUser;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/post")
//@RequiredArgsConstructor
//@Tag(name = "Post")
//public class PostController {
//
//    private final PostService postService;
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Post> getPostById(@PathVariable Long id, Authentication auth) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        OAuth2AuthenticationToken authorities = (OAuth2AuthenticationToken) authentication.getAuthorities();
//        if (authentication != null) {
//            System.out.println(authentication.getName());
//        }
//
//        Post post = postService.getPostById(id);
//        return post != null ? ResponseEntity.ok(post) : ResponseEntity.notFound().build();
//    }
//
//    @GetMapping("/{id}/postAnswers")
//    public ResponseEntity<List<Answer>> getPostAnswers(@PathVariable Long id) {
//        List<Answer> postAnswers = postService.getPostAnswers(id);
//
//        return postAnswers != null ? ResponseEntity.ok(postAnswers) : ResponseEntity.notFound().build();
//    }
//
//    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
//    @PreAuthorize("hasAnyAuthority('OIDC_USER')")
//    public Post createPost(@RequestBody PostCreateDto postCreateDto, Authentication auth) {
//        DefaultOidcUser oidcUser = (DefaultOidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        if (oidcUser instanceof CustomOidcUser) {
//            Long userId = ((CustomOidcUser) oidcUser).getUserId();
//            return postService.savePost(postCreateDto, userId);
//        }
//
//        throw new IllegalStateException("User is not authenticated");
//
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
//        postService.deletePost(id);
//        return ResponseEntity.noContent().build();
//    }
//}
