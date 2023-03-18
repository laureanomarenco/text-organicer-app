package com.textorganicer.utils;

import org.mindrot.jbcrypt.BCrypt;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class HashGenerator {

    public static String hashPassword(String password, byte[] salt) {
        int rounds = 10;
        String hashedPassword = BCrypt.hashpw(addSaltToPassword(password, salt), BCrypt.gensalt(rounds));
        return hashedPassword;
    }

    public static String addSaltToPassword(String password, byte[] salt) {
        StringBuilder sb = new StringBuilder();
        sb.append(new String(salt, StandardCharsets.UTF_8)); // Agregar la salt al inicio de la contraseña
        sb.append(password);
        return sb.toString();
    }

    public static boolean verifyPassword(String password, byte[] salt, String hashedPassword) {
        String saltedPassword = addSaltToPassword(password, salt);
        return BCrypt.checkpw(saltedPassword, hashedPassword);
    }

}
