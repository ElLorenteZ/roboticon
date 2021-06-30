package io.lorentez.roboticon.passwords;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncryptionTests {

    @Test
    void testBcrypt() {
        PasswordEncoder bcrypt = new BCryptPasswordEncoder();
        System.out.println(bcrypt.encode("testtest"));
    }

}
