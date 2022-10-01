package fr.marketplace.bi;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Email(String email) {

    public final static Pattern EMAIL_PATTERN = Pattern.compile("^(.+)@(.+)$");

    public Email {
        Objects.requireNonNull(email);
        final Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (!matcher.matches()) throw new EmailBadPatternException();
    }

    public static Email of(String email) {
        return new Email(email);
    }

    public static class EmailBadPatternException extends RuntimeException {
        public EmailBadPatternException() {
            super("The pattern of email is bad");
        }
    }
}
