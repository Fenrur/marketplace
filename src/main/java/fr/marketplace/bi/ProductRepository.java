package fr.marketplace.bi;

import com.fasterxml.jackson.core.type.TypeReference;
import fr.marketplace.Json;
import org.eclipse.collections.api.multimap.set.MutableSetMultimap;
import org.eclipse.collections.impl.factory.Multimaps;

import java.nio.file.Path;
import java.util.Set;
import java.util.UUID;

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
        MutableSetMultimap<UUID, Product> repository;
        try {
            repository = json.decodeFromFile(productRepositoryFilePath, new TypeReference<>() {
            });
        } catch (Exception ignored) {
            repository = Multimaps.mutable.set.empty();
        }

        return new ProductRepository(json, repository, productRepositoryFilePath);
    }

    private void writeToFile() {
        json.encodeToFile(filePath, repository);
    }

    public void put(UUID userId, Product product) {
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
        allProducts.removeIf(product -> !product.isAvailable());

        for (Search search : productSearch.searches()) {
            if (allProducts.isEmpty()) break;
            search.accept(allProducts);
        }

        return allProducts;
    }
}
