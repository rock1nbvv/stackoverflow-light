package rockinbvv.stackoverflowlight.system.security;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import rockinbvv.stackoverflowlight.app.data.model.User;
import rockinbvv.stackoverflowlight.app.repository.UserRepository;

import java.util.Optional;
import java.util.Set;


@Service
public class CustomOidcUserService extends OidcUserService {

    private final UserRepository userRepository;

    public CustomOidcUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        OidcUser oidcUser = super.loadUser(userRequest);

        String email = oidcUser.getAttribute("email");
        String googleId = oidcUser.getAttribute("sub"); // Google User ID

        // Fetch the user from the database using email or googleId
        Optional<User> optionalUser = userRepository.findOneByEmail(email);
        User user;

        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            // Register the user if not found
            user = User.builder()
                .email(email)
                .googleId(googleId)
                .name(oidcUser.getAttribute("name"))
                .build();
            user = userRepository.save(user);
        }

        // Create CustomOidcUser with userId included
        return new CustomOidcUser(
            user.getId(),
            Set.copyOf(oidcUser.getAuthorities()),
            oidcUser.getIdToken(),
            user.getEmail()
        );
    }
}

//@Service
//@RequiredArgsConstructor
//public class CustomOidcUserService extends OidcUserService {
//
//    private final UserRepository userRepository;
//
//    @Override
//    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
//        OidcUser oidcUser = super.loadUser(userRequest);
//        try {
//            return processOidcUser(userRequest, oidcUser);
//        } catch (AuthenticationException ex) {
//            throw ex;
//        } catch (Exception ex) {
//            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
//            throw new OAuth2AuthenticationException(ex.getMessage());
//        }
//    }
//
//
//    private OidcUser processOidcUser(OidcUserRequest userRequest, OidcUser oidcUser) {
//        String registrationId = userRequest.getClientRegistration().getRegistrationId();
//        String email = oidcUser.getAttribute("email");
//        if (email == null || email.isBlank()) {
//            throw new OAuth2AuthenticationException("can't retrieve an email address");
//        }
//
//        Optional<User> userOptional = userRepository.findOneByEmailEqualsIgnoreCase(email);
//        User user;
//        if (userOptional.isPresent()) {
//            user = userOptional.get();
//            updateExistingUser(user, oidcUser);
//        } else {
//            registerNewUser(userRequest, oidcUser);
//        }
//
//        return super.loadUser(userRequest);
//    }
//
//    private void registerNewUser(OidcUserRequest userRequest, OidcUser oidcUser) {

/// /        todo research these fields
/// /        oAuth2UserRequest.getClientRegistration().getRegistrationId();
/// /        oAuth2UserInfo.getAttribute("id");
//
//        User user = new User();
//        user.setName(oidcUser.getAttribute("name"));
//        user.setEmail(oidcUser.getAttribute("email"));
//        userRepository.save(user);
//    }
//
//    private void updateExistingUser(User existingUser, OAuth2User oAuth2UserInfo) {
//        existingUser.setName(oAuth2UserInfo.getAttribute("name"));
//        userRepository.save(existingUser);
//    }
//}
