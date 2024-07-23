package rockinbvv.stackoverflowlight.system;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf(csrfConfigurer -> {
//                    try {
//                        csrfConfigurer.disable().authorizeHttpRequests(
//                                authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
//                                        .requestMatchers("/api/**")
//                                        .permitAll()
//                                        .anyRequest().authenticated()
//                        );
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//        );
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
