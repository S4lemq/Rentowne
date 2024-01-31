package pl.rentowne.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PasswordGenerator {

    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String PUNCTUATION = "!@#$%&*()_+-=[]|,./?><";
    private static final String ALLOWED_CHARACTERS = LOWER + UPPER + DIGITS + PUNCTUATION;
    private static final int PASSWORD_LENGTH = 32;

    public static String generatePassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

        // Dodajemy po jednym znaku z każdej grupy, aby spełnić wymagania
        password.append(LOWER.charAt(random.nextInt(LOWER.length())));
        password.append(UPPER.charAt(random.nextInt(UPPER.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(PUNCTUATION.charAt(random.nextInt(PUNCTUATION.length())));

        // Uzupełniamy pozostałą część hasła losowymi znakami
        for (int i = 4; i < PASSWORD_LENGTH; i++) {
            password.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        }

        // Tasujemy hasło, aby uniknąć przewidywalnych wzorców
        List<Character> characters = new ArrayList<>();
        for (char c : password.toString().toCharArray()) {
            characters.add(c);
        }
        Collections.shuffle(characters);

        StringBuilder finalPassword = new StringBuilder(PASSWORD_LENGTH);
        for (char c : characters) {
            finalPassword.append(c);
        }

        return finalPassword.toString();
    }

}
