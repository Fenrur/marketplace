package fr.marketplace.bi;

import com.fasterxml.jackson.core.type.TypeReference;
import fr.marketplace.Json;
import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.api.factory.Bags;

import java.nio.file.Path;
import java.util.UUID;

public class StockProductRepository {

    private final Json json;
    private final MutableBag<UUID> repository;
    private final Path stockProductRepositoryFilePath;

    public StockProductRepository(Json json, MutableBag<UUID> repository, Path stockProductRepositoryFilePath) {
        this.json = json;
        this.repository = repository;
        this.stockProductRepositoryFilePath = stockProductRepositoryFilePath;
    }

    public static StockProductRepository fromFile(Json json, Path stockProductRepositoryFilePath) {
        MutableBag<UUID> repository;
        try {
            repository = json.decodeFromFile(stockProductRepositoryFilePath, new TypeReference<>() {
            });
        } catch (Exception e) {
            repository = Bags.mutable.empty();
        }

        return new StockProductRepository(json, repository, stockProductRepositoryFilePath);
    }

    private void writeToFile() {
        json.encodeToFile(stockProductRepositoryFilePath, repository);
    }

    public boolean has(UUID productId, int amount) {
        return getAmount(productId) >= amount;
    }

    public int getAmount(UUID productId) {
        return repository.occurrencesOf(productId);
    }

    public void removeAmount(UUID productId, int amount) {
        repository.removeOccurrences(productId, amount);
        writeToFile();
    }

    public void addAmount(UUID productId, int amount) {
        repository.addOccurrences(productId, amount);
        writeToFile();
    }
}
