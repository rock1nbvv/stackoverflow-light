package rockinbvv.stackoverflowlight.system.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.util.Collection;

public class CustomOidcUser extends DefaultOidcUser {
    private final Long userId;
    private final String email;

    public CustomOidcUser(Long userId, Collection<? extends GrantedAuthority> authorities, OidcIdToken idToken, String email) {
        super(authorities, idToken, "email"); // "email" is used as the attribute key for the name
        this.userId = userId;
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }
}
