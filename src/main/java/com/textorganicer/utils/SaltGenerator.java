package com.textorganicer.utils;

import java.security.SecureRandom;

public class SaltGenerator {
    public static byte[] generateSalt() {
        final Integer LENGTH_OF_SALT = 6;

        byte[] salt = new byte[LENGTH_OF_SALT];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }
}
