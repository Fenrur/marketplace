package fr.marketplace.bi;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

public record User(UUID id, Email email, Username username, HashedPassword hashedPassword, Type type,
                   boolean isSubscriber,
                   ZonedDateTime createAt) {

    public User {
        Objects.requireNonNull(id);
        Objects.requireNonNull(email);
        Objects.requireNonNull(username);
        Objects.requireNonNull(hashedPassword);
        Objects.requireNonNull(type);
        Objects.requireNonNull(createAt);
    }

    public static void main(String[] args) {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id.equals(user.id);
    }

    public enum Type {
        CUSTOMER,
        SELLER,
        MARKETPLACE,
        DELIVERY
    }
}
