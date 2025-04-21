package rockinbvv.stackoverflowlight.system.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserProvider {

    public AuthenticatedUser get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomOidcUser customOidcUser) {
            return new AuthenticatedUser(customOidcUser.getUserId(), customOidcUser.getName(), authentication.getAuthorities());
        }

        if (principal instanceof UserDetails userDetails) {
            // Traditional login users (e.g., username/password)
            // need to resolve userId from DB if needed
            Long userId = resolveUserIdFromUsername(userDetails.getUsername());
            return new AuthenticatedUser(userId, userDetails.getUsername(), userDetails.getAuthorities());
        }

        throw new IllegalStateException("Unsupported authentication principal: " + principal.getClass());
    }

    private Long resolveUserIdFromUsername(String username) {
        // Inject UserDao and implement lookup logic here
        throw new UnsupportedOperationException("Implement user lookup by username");
    }
}

