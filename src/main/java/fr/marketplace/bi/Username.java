package fr.marketplace.bi;

import java.util.Objects;

public record Username(String username) implements Comparable<Username> {

    public Username {
        Objects.requireNonNull(username);
        if (username.length() < 5) throw new UsernameBadPatternException();
    }

    public static Username of(String username) {
        return new Username(username);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Username username1 = (Username) o;

        return username.equalsIgnoreCase(username1.username);
    }

    @Override
    public String toString() {
        return username;
    }

    @Override
    public int compareTo(Username o) {
        return username.compareTo(o.username);
    }

    public static class UsernameBadPatternException extends RuntimeException {
        public UsernameBadPatternException() {
            super("The pattern of username must be minimum has 5 characters");
        }
    }
}
