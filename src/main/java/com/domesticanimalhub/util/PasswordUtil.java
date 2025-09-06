package com.domesticanimalhub.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    private static final int ROUNDS = 10;

    public static String hash(String plain) {
        return BCrypt.hashpw(plain, BCrypt.gensalt(ROUNDS));
    }

    public static boolean check(String plain, String hash) {
        if (plain == null || hash == null) return false;
        return BCrypt.checkpw(plain, hash);
    }
}
