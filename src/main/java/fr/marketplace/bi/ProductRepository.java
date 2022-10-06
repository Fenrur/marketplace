package fr.marketplace.bi;

import com.fasterxml.jackson.core.type.TypeReference;
import fr.marketplace.Json;
import org.eclipse.collections.api.RichIterable;
import org.eclipse.collections.api.multimap.set.MutableSetMultimap;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Multimaps;

import java.nio.file.Path;
import java.util.*;

public class ProductRepository {

    private final MutableSetMultimap<UUID, Product> repository;
    private final Json json;
    private final Path filePath;

    private ProductRepository(Json json, MutableSetMultimap<UUID, Product> repository, Path filePath) {
        this.repository = repository;
        this.json = json;
        this.filePath = filePath;
    }

    public static ProductRepository fromFile(Json json, Path productRepositoryFilePath) {
        Map<UUID, Set<Product>> map;
        try {
            map = json.decodeFromFile(productRepositoryFilePath, new TypeReference<>() {
            });
        } catch (Exception ignored) {
            map = new HashMap<>();
        }

        final MutableSetMultimap<UUID, Product> repository = Multimaps.mutable.set.empty();

        map.forEach(repository::putAll);

        return new ProductRepository(json, repository, productRepositoryFilePath);
    }

    private void writeToFile() {
        // small trick no serializer / deserializer for MutableSetMultimap
        final Map<UUID, RichIterable<Product>> toWrite = new HashMap<>();

        repository.forEachKeyMultiValues(toWrite::put);

        json.encodeToFile(
                filePath,
                toWrite
        );
    }

    public Set<Product> getAllProductByUserId(UUID userId) {
        return repository.get(userId).toSet();
    }

    public void put(UUID userId, Product product) {
        repository.remove(userId, product);
        repository.put(userId, product);
        writeToFile();
    }

    public Set<Product> getAllProducts() {
        return repository
                .valuesView()
                .toSet();
    }

    public Set<Product> search(ProductSearch productSearch) {
        final Set<Product> allProducts = getAllProducts();

        for (Search search : productSearch.searches()) {
            if (allProducts.isEmpty()) break;
            search.accept(allProducts);
        }

        return allProducts;
    }

    public Optional<UUID> getUserIdByProductId(UUID productId) {
        final RichIterable<UUID> userIds = repository.keysView();

        for (UUID userId : userIds) {
            final MutableSet<Product> products = repository.get(userId);
            for (Product product : products) {
                if (product.id().equals(productId)) return Optional.of(userId);
            }
        }

        return Optional.empty();
    }

    public Optional<Product> get(UUID productId) {
        return getAllProducts()
                .stream()
                .filter(product -> product.id().equals(productId))
                .findFirst();
    }
}
