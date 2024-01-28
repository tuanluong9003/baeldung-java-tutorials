package com.baeldung.uuid;

import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

import org.apache.commons.lang3.Conversion;
import org.junit.jupiter.api.Test;

public class DecodeUUIDStringFromBase64Test {
    private final UUID originalUUID = UUID.fromString("cc5f93f7-8cf1-4a51-83c6-e740313a0c6c");

    @Test
    public void shouldDecodeUUIDUsingByteArrayAndBase64Decoder() {
        byte[] decodedBytes = Base64.getDecoder()
          .decode("UUrxjPeTX8xsDDoxQOfGgw");
        UUID uuid = convertToUUID(decodedBytes);
        assertEquals(originalUUID, uuid);
    }

    @Test
    public void shouldDecodeUUIDUsingByteBufferAndBase64UrlDecoder() {
        byte[] decodedBytes = Base64.getUrlDecoder()
          .decode("zF-T94zxSlGDxudAMToMbA");
        ByteBuffer byteBuffer = ByteBuffer.wrap(decodedBytes);
        long mostSignificantBits = byteBuffer.getLong();
        long leastSignificantBits = byteBuffer.getLong();
        UUID uuid = new UUID(mostSignificantBits, leastSignificantBits);
        assertEquals(originalUUID, uuid);
    }

    @Test
    public void shouldDecodeUUIDUsingApacheUtils() {
        byte[] decodedBytes = decodeBase64("UUrxjPeTX8xsDDoxQOfGgw");
        UUID uuid = Conversion.byteArrayToUuid(decodedBytes, 0);
        assertEquals(originalUUID, uuid);
    }

    private UUID convertToUUID(byte[] src) {
        long mostSignificantBits = convertBytesToLong(src, 0);
        long leastSignificantBits = convertBytesToLong(src, 8);

        return new UUID(mostSignificantBits, leastSignificantBits);
    }

    private long convertBytesToLong(byte[] uuidBytes, int start) {
        long result = 0;

        for(int i = 0; i < 8; i++) {
            int shift = i * 8;
            long bits = (255L & (long)uuidBytes[i + start]) << shift;
            long mask = 255L << shift;
            result = result & ~mask | bits;
        }

        return result;
    }
}
