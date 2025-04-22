package rockinbvv.stackoverflowlight.system.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import rockinbvv.stackoverflowlight.app.dao.UserDao;
import rockinbvv.stackoverflowlight.app.exception.EntityNotFoundException;
import rockinbvv.stackoverflowlight.app.exception.EntityType;

@Component
@RequiredArgsConstructor
public class CurrentUserProvider {

    private final UserDao userDao;

    public AuthenticatedUser get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomOidcUser customOidcUser) {
            return new AuthenticatedUser(customOidcUser.getUserId(), customOidcUser.getName(), authentication.getAuthorities());
        }

        if (principal instanceof CustomUserDetails customUserDetails) {
            return new AuthenticatedUser(customUserDetails.getUserId(), customUserDetails.getUsername(), authentication.getAuthorities());
        }

        if (principal instanceof UserDetails userDetails) {
            // Traditional login users (e.g., username/password)
            Long userId = resolveUserIdFromUsername(userDetails.getUsername());
            return new AuthenticatedUser(userId, userDetails.getUsername(), authentication.getAuthorities());
        }

        if (principal instanceof String username) {
            // For cases where principal is just a username string
            Long userId = resolveUserIdFromUsername(username);
            return new AuthenticatedUser(userId, username, authentication.getAuthorities());
        }

        throw new IllegalStateException("Unsupported authentication principal: " + principal.getClass());
    }

    private Long resolveUserIdFromUsername(String username) {
        return userDao.findByEmail(username)
            .map(user -> user.getId())
            .orElseThrow(() -> new EntityNotFoundException(EntityType.USER, "email", username));
    }
}
