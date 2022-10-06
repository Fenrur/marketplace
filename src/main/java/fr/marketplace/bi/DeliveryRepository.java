package fr.marketplace.bi;

import com.fasterxml.jackson.core.type.TypeReference;
import fr.marketplace.Json;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class DeliveryRepository {

    private final Map<UUID, DeliveryInformation> repository;
    private final Json json;
    private final Path filePath;

    private DeliveryRepository(Json json, Map<UUID, DeliveryInformation> repository, Path filePath) {
        this.repository = repository;
        this.json = json;
        this.filePath = filePath;
    }

    public static DeliveryRepository fromFile(Json json, Path productRepositoryFilePath) {
        Map<UUID, DeliveryInformation> repository;
        try {
            repository = json.decodeFromFile(productRepositoryFilePath, new TypeReference<>() {
            });
        } catch (Exception ignored) {
            repository = new HashMap<>();
        }

        return new DeliveryRepository(json, repository, productRepositoryFilePath);
    }

    public DeliveryInformation get(UUID deliveryProductId) {
        return repository.get(deliveryProductId);
    }

    public void put(UUID deliveryProductId, DeliveryInformation deliveryInformation) {
        repository.put(deliveryProductId, deliveryInformation);
        writeToFile();
    }

    private void writeToFile() {
        json.encodeToFile(filePath, repository);
    }

    public LocalDate getEstimatedDate(PostalAddress postalAddress) {
        final LocalDate now = LocalDate.now();
        final Random random = new Random();
        final LocalDate estimatedDate = now.plus(random.nextInt(14) + 1, ChronoUnit.DAYS);
        return estimatedDate;
    }
}
