package com.petcare.petowner.util;

import com.petcare.petowner.exception.Sha256Exception;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AddressHashUtilTest {

    @Test
    void testNormalizeAndHash_ShouldReturnConsistentHash() throws Sha256Exception {
        String city = "Pune";
        String type = "Street";
        String addressName = "MG Road";
        String number = "101";

        String hash1 = AddressHashUtil.normalizeAndHash(city, type, addressName, number);
        String hash2 = AddressHashUtil.normalizeAndHash("  pune ", "street", "mg road ", " 101");

        assertNotNull(hash1);
        assertEquals(hash1, hash2, "Hash should be consistent for normalized input");
        assertEquals(64, hash1.length(), "SHA-256 hash should be 64 hex characters long");
    }

    @Test
    void testNormalizeAndHash_WithEmptyStrings() throws Sha256Exception {
        String hash = AddressHashUtil.normalizeAndHash("", "", "", "");
        assertNotNull(hash);
        assertEquals(64, hash.length());
    }

    @Test
    void testNormalizeAndHash_ShouldTrimAndLowercaseInputs() throws Sha256Exception {
        String hash1 = AddressHashUtil.normalizeAndHash("Mumbai", "Avenue", "Carter Rd", "12");
        String hash2 = AddressHashUtil.normalizeAndHash("  mumbai ", "avenue", "carter rd", "12 ");
        assertEquals(hash1, hash2, "Normalization should remove whitespace and make lowercase");
    }
}

