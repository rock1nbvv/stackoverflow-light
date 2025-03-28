package rockinbvv.stackoverflowlight.system;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("OAuth"))
                .components(
                        new Components()
                                .addSecuritySchemes("OAuth",
                                        new SecurityScheme()
                                                .name("OAuth")
                                                .type(SecurityScheme.Type.OPENIDCONNECT)
                                                .openIdConnectUrl("https://accounts.google.com/.well-known/openid-configuration")
                                )
                );
    }
}
