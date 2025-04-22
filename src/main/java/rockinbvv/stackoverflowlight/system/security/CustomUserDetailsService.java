package rockinbvv.stackoverflowlight.system.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rockinbvv.stackoverflowlight.app.dao.UserDao;
import rockinbvv.stackoverflowlight.app.dao.UserAuthDao;
import rockinbvv.stackoverflowlight.app.data.user.UserFullResponseDto;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDao userDao;
    private final UserAuthDao userAuthDao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserFullResponseDto user = userDao.findFullUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Check if user has password authentication
        if (user.getPassword() == null) {
            throw new UsernameNotFoundException("User does not have password authentication");
        }

        // Create authorities list
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        
        // Add LOCAL_USER authority for form-based authentication
        authorities.add(new SimpleGrantedAuthority("LOCAL_USER"));
        
        // Add ROLE_ADMIN if user is admin
        if (user.getIsAdmin() != null && user.getIsAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        // Create UserDetails object with email as username, password, and authorities
        return new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}