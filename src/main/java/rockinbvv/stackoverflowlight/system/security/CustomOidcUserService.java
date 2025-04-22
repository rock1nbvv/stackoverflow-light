package rockinbvv.stackoverflowlight.system.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import rockinbvv.stackoverflowlight.app.dao.UserAuthDao;
import rockinbvv.stackoverflowlight.app.dao.UserDao;
import rockinbvv.stackoverflowlight.app.data.user.OidcUserResponseDto;
import rockinbvv.stackoverflowlight.app.data.user.UserCreateDto;
import rockinbvv.stackoverflowlight.app.service.UserService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

    private final UserDao userDao;
    private final UserService userService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        OidcUser oidcUser = super.loadUser(userRequest);

        String email = oidcUser.getAttribute("email");
        String googleId = oidcUser.getAttribute("sub");
        Optional<OidcUserResponseDto> optionalUser = userDao.findOidcUserByEmail(email);

        Long userId;
        Set<GrantedAuthority> authorities = new HashSet<>(oidcUser.getAuthorities());

        if (optionalUser.isPresent()) {
            OidcUserResponseDto user = optionalUser.get();
            userId = user.getId();

            // Add ROLE_ADMIN authority if the user is an admin
            if (user.getIsAdmin() != null && user.getIsAdmin()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }
        } else {
            userId = userService.createUser(UserCreateDto.builder()
                    .email(email)
                    .googleId(googleId)
                    .name(oidcUser.getAttribute("name"))
                    .build()
            );
        }

        return new CustomOidcUser(
                userId,
                authorities,
                oidcUser.getIdToken(),
                email
        );
    }
}
