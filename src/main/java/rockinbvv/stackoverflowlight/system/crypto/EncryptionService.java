package rockinbvv.stackoverflowlight.system.crypto;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class EncryptionService {
    public String encrypt(String plainText) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        return encoder.encode(plainText);
    }

    public Boolean decrypt(String plainText, String encryptedText) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        return encoder.matches(plainText, encryptedText);
    }
}
