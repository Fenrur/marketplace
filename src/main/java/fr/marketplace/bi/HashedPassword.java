package fr.marketplace.bi;

import fr.marketplace.SHA256Hashing;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record HashedPassword(String hash) {

    private static final char[] HEXADECIMALS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'A', 'B', 'C', 'D', 'E', 'F'};

    private final static Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");

    public HashedPassword {
        Objects.requireNonNull(hash);
        if (hash.length() != 64 || !StringUtils.containsOnly(hash, HEXADECIMALS))
            throw new HashedPasswordBadPatternException();
    }

    public static HashedPassword of(String hashedPassword) {
        return new HashedPassword(hashedPassword);
    }


    public static HashedPassword hash(String password) {
        Objects.requireNonNull(password);
        final Matcher matcher = PASSWORD_PATTERN.matcher(password);
        if (!matcher.matches()) {
            throw new PasswordBadPatternException();
        }

        return new HashedPassword(SHA256Hashing.hashHex(password));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HashedPassword that = (HashedPassword) o;

        return hash.equalsIgnoreCase(that.hash);
    }

    public static class HashedPasswordBadPatternException extends RuntimeException {

        public HashedPasswordBadPatternException() {
            super("is not valid sha256 hash");
        }
    }

    public static class PasswordBadPatternException extends RuntimeException {

    }
}
