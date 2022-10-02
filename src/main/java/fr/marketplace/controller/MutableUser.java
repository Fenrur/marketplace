package fr.marketplace.controller;

import fr.marketplace.bi.*;

import java.time.ZonedDateTime;
import java.util.UUID;

public class MutableUser {

    private UUID id;
    private Email email;
    private Username username;
    private HashedPassword hashedPassword;
    private User.Type type;
    private boolean isSubscriber;
    private ZonedDateTime createAt;
    private boolean isDisable;

    public MutableUser() {
    }

    public MutableUser(UUID id, Email email, Username username, HashedPassword hashedPassword, User.Type type, boolean isSubscriber, ZonedDateTime createAt, boolean isDisable) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.type = type;
        this.isSubscriber = isSubscriber;
        this.createAt = createAt;
        this.isDisable = isDisable;
    }

    public static MutableUser from(User user) {
        return new MutableUser(user.id(), user.email(), user.username(), user.hashedPassword(), user.type(), user.isSubscriber(), user.createAt(), user.isDisable());
    }

    public UUID getId() {
        return id;
    }

    public MutableUser setId(UUID id) {
        this.id = id;
        return this;
    }

    public Email getEmail() {
        return email;
    }

    public MutableUser setEmail(Email email) {
        this.email = email;
        return this;
    }

    public Username getUsername() {
        return username;
    }

    public MutableUser setUsername(Username username) {
        this.username = username;
        return this;
    }

    public HashedPassword getHashedPassword() {
        return hashedPassword;
    }

    public MutableUser setHashedPassword(HashedPassword hashedPassword) {
        this.hashedPassword = hashedPassword;
        return this;
    }

    public User.Type getType() {
        return type;
    }

    public MutableUser setType(User.Type type) {
        this.type = type;
        return this;
    }

    public boolean isSubscriber() {
        return isSubscriber;
    }

    public MutableUser setSubscriber(boolean subscriber) {
        isSubscriber = subscriber;
        return this;
    }

    public ZonedDateTime getCreateAt() {
        return createAt;
    }

    public MutableUser setCreateAt(ZonedDateTime createAt) {
        this.createAt = createAt;
        return this;
    }

    public boolean isDisable() {
        return isDisable;
    }

    public MutableUser setDisable(boolean disable) {
        isDisable = disable;
        return this;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public User build() {
        return new User(this.id, this.email, this.username, this.hashedPassword, this.type, this.isSubscriber, this.createAt, this.isDisable);
    }
}
