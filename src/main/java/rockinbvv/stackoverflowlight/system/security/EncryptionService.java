package rockinbvv.stackoverflowlight.system.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class EncryptionService {

    private final BCryptPasswordEncoder encoder;

    public EncryptionService() {
        this.encoder = new BCryptPasswordEncoder(8);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return encoder;
    }

    public String encrypt(String plainText) {
        return encoder.encode(plainText);
    }

    public Boolean verifyPassword(String plainText, String hashedPassword) {
        return encoder.matches(plainText, hashedPassword);
    }
}
