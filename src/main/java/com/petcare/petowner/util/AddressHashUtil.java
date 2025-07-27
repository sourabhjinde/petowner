package com.petcare.petowner.util;


import com.petcare.petowner.exception.Sha256Exception;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AddressHashUtil {
    private AddressHashUtil(){}

    public static String normalizeAndHash(String city, String type, String addressName, String number) throws Sha256Exception{
        String normalized = String.join("|",
                city.trim().toLowerCase(),
                type.trim().toLowerCase(),
                addressName.trim().toLowerCase(),
                number.trim().toLowerCase()
        );
        return sha256(normalized);
    }

    private static String sha256(String input) throws Sha256Exception {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new Sha256Exception("SHA-256 not supported", e);
        }
    }
}
