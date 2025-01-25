package rockinbvv.stackoverflowlight.system.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import rockinbvv.stackoverflowlight.app.data.model.User;
import rockinbvv.stackoverflowlight.app.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

    private final UserRepository userRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        try {
            return processOidcUser(userRequest, oidcUser);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new OAuth2AuthenticationException(ex.getMessage());
        }
    }


    private OidcUser processOidcUser(OidcUserRequest userRequest, OidcUser oidcUser) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String email = oidcUser.getAttribute("email");
        if (email == null || email.isBlank()) {
            throw new OAuth2AuthenticationException("can't retrieve an email address");
        }

        Optional<User> userOptional = userRepository.findOneByEmailEqualsIgnoreCase(email);
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            updateExistingUser(user, oidcUser);
        } else {
            registerNewUser(userRequest, oidcUser);
        }

        return super.loadUser(userRequest);
    }

    private void registerNewUser(OidcUserRequest userRequest, OidcUser oidcUser) {
//        todo research these fields
//        oAuth2UserRequest.getClientRegistration().getRegistrationId();
//        oAuth2UserInfo.getAttribute("id");

        User user = new User();
        user.setName(oidcUser.getAttribute("name"));
        user.setEmail(oidcUser.getAttribute("email"));
        userRepository.save(user);
    }

    private void updateExistingUser(User existingUser, OAuth2User oAuth2UserInfo) {
        existingUser.setName(oAuth2UserInfo.getAttribute("name"));
        userRepository.save(existingUser);
    }
}
