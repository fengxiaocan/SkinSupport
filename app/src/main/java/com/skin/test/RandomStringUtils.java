package com.skin.test;

import java.util.Random;

public class RandomStringUtils {
    private static final int UNICODE_RANDOM = 0xFFFF + 1;
    private static final int BASE_RANDOM = 0x9fa5 - 0x4e00 + 1;
    private static final Random random = new Random();

    private static char getRandomGbChar() {
        return (char) (0x4e00 + random.nextInt(BASE_RANDOM));
    }

    private static char getRandomUnicodeChar() {
        return (char) (random.nextInt(UNICODE_RANDOM));
    }

    private static char getAsciiChar() {
        return (char)(random.nextInt(128));
    }

    private static char getWordChar() {
        return (char)(random.nextInt(78)+49);
    }

    public static final int randomInt(int bound){
        return random.nextInt(bound);
    }

    public static final  String randomGBString(int lenght) {
        if (lenght > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < lenght; i++) {
                sb.append(getRandomGbChar());
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    public static final  String randomString(int lenght) {
        if (lenght > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < lenght; i++) {
                int i1 = random.nextInt(10);
                if (i1<2){
                    sb.append(getRandomGbChar());
                }else {
                    sb.append(getWordChar());
                }
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    public static final String randomUnicodeString(int lenght) {
        if (lenght > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < lenght; i++) {
                sb.append(getRandomUnicodeChar());
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    public static final String randomAsciiString(int lenght) {
        if (lenght > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < lenght; i++) {
                sb.append(getAsciiChar());
            }
            return sb.toString();
        } else {
            return "";
        }
    }
}
