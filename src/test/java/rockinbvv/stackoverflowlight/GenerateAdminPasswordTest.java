package rockinbvv.stackoverflowlight;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerateAdminPasswordTest {

    @Test
    void generateAdminPassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(17);
        String myPassword = encoder.encode("myPassword");
        String checkPassword = "myPassword";
        System.out.println(myPassword);
//        Assertions.assertTrue(encoder.matches("$2a$17$VsLUuRRpv9L6hfDIvFRN0.stLC4nWMiuX9Kx8Uc2rGQVCM2G.nfx6", myPassword));
        Assertions.assertTrue(encoder.matches(checkPassword, "$2a$17$VsLUuRRpv9L6hfDIvFRN0.stLC4nWMiuX9Kx8Uc2rGQVCM2G.nfx6"));
    }

}
