package fr.marketplace.bi;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Email(String email) implements Comparable<Email> {

    public final static Pattern EMAIL_PATTERN = Pattern.compile("^(.+)@(.+)$");

    public Email {
        Objects.requireNonNull(email);
        final Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (!matcher.matches()) throw new EmailBadPatternException();
    }

    public static Email of(String email) {
        return new Email(email);
    }

    @Override
    public int compareTo(Email o) {
        return email.compareTo(o.email);
    }

    @Override
    public String toString() {
        return email;
    }

    public static class EmailBadPatternException extends RuntimeException {
        public EmailBadPatternException() {
            super("The pattern of email is bad");
        }
    }
}
