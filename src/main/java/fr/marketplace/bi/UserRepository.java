package fr.marketplace.bi;

import com.fasterxml.jackson.core.type.TypeReference;
import fr.marketplace.Json;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class UserRepository implements Iterable<User> {

    private final Json json;
    private final Path userFilePath;
    private final Set<User> repository;

    public UserRepository(Json json, Set<User> repository, Path filePath) {
        this.json = json;
        this.userFilePath = filePath;
        this.repository = repository;
    }

    public static UserRepository fromFile(Json json, Path userRepositoryFilePath) {
        Set<User> repository;
        try {
            repository = json.decodeFromFile(userRepositoryFilePath, new TypeReference<>() {
            });
        } catch (Exception ignored) {
            repository = new LinkedHashSet<>();
        }

        return new UserRepository(json, repository, userRepositoryFilePath);
    }

    public void put(User user) {
        repository.remove(user);
        repository.add(user);
        writeToFile();
    }

    private void writeToFile() {
        json.encodeToFile(userFilePath, repository);
    }

    public Set<User> getAllUsers() {
        return new HashSet<>(repository);
    }

    public Stream<User> stream() {
        return repository.stream();
    }

    @Override
    public Iterator<User> iterator() {
        return repository.iterator();
    }

    public Optional<User> get(Email email) {
        return stream()
                .filter(user -> user.email().equals(email))
                .findFirst();
    }

    public Optional<User> get(Username username) {
        return stream()
                .filter(user -> user.username().equals(username))
                .findFirst();
    }

    public Optional<User> get(UUID userId) {
        return stream()
                .filter(user -> user.id().equals(userId))
                .findFirst();
    }
}
