package com.projectmanagement.security;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class to generate a secure key for signing JWT tokens.
 */
public class SecureKeyGenerator {

    private static final int KEY_SIZE = 32; // 256 bits (32 bytes)

    /**
     * Generates a secure random key suitable for signing JWT tokens.
     *
     * @return a Base64-encoded secure key as a String
     */
    public static String generateSecureKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[KEY_SIZE];
        secureRandom.nextBytes(keyBytes);
        return Base64.getEncoder().encodeToString(keyBytes);
    }

    // Test the generator (Optional: Remove this main method in production)
    public static void main(String[] args) {
        String secureKey = generateSecureKey();
        System.out.println("Generated Secure Key: " + secureKey);
    }
}
