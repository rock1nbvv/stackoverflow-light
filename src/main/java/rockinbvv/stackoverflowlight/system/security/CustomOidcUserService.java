package rockinbvv.stackoverflowlight.system.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import rockinbvv.stackoverflowlight.app.dao.UserDao;
import rockinbvv.stackoverflowlight.app.data.User;
import rockinbvv.stackoverflowlight.app.data.dto.user.request.CreateUserDto;

import java.util.Optional;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

    private final UserDao userDao;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        OidcUser oidcUser = super.loadUser(userRequest);

        String email = oidcUser.getAttribute("email");
        String googleId = oidcUser.getAttribute("sub");
        Optional<User> optionalUser = userDao.findOneByEmail(email);
        Long userId = optionalUser.map(User::getId)
                .orElse(
                        userDao.registerUser(
                                CreateUserDto.builder()
                                        .email(email)
                                        .googleId(googleId)
                                        .name(oidcUser.getAttribute("name"))
                                        .build()
                        )
                );
        return new CustomOidcUser(
                userId,
                Set.copyOf(oidcUser.getAuthorities()),
                oidcUser.getIdToken(),
                email
        );
    }
}
