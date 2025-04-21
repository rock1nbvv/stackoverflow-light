package rockinbvv.stackoverflowlight.system.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public record AuthenticatedUser(Long userId, String username, Collection<? extends GrantedAuthority> authorities) {}
