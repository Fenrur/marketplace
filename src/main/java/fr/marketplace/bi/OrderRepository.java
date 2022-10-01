package fr.marketplace.bi;

import com.fasterxml.jackson.core.type.TypeReference;
import fr.marketplace.Json;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class OrderRepository implements Iterable<Order> {

    private final Map<UUID, Order> repository;
    private final Path orderRepositoryFilePath;
    private final Json json;

    public OrderRepository(Json json, Map<UUID, Order> repository, Path orderRepositoryFilePath) {
        this.json = json;
        this.repository = repository;
        this.orderRepositoryFilePath = orderRepositoryFilePath;
    }

    public static OrderRepository fromFile(Json json, Path orderRepositoryFilePath) {
        Set<Order> orders;
        try {
            orders = json.decodeFromFile(orderRepositoryFilePath, new TypeReference<>() {
            });
        } catch (Exception e) {
            orders = new HashSet<>();
        }

        final Map<UUID, Order> repository = new LinkedHashMap<>();
        orders.forEach(order -> repository.put(order.orderId(), order));

        return new OrderRepository(json, repository, orderRepositoryFilePath);
    }

    public Order createOrder(StockProductRepository stockProductRepository, List<ShoppingProduct> shoppingProducts, UUID customerId, PostalAddress postalAddress, CreditCart creditCart) {
        if (shoppingProducts.isEmpty()) throw new IllegalArgumentException("No element in ShoppingCart");

        final UUID orderId = UUID.randomUUID();

        final Set<DeliveryProduct> deliveryProducts = shoppingProducts
                .stream()
                .map(shoppingProduct -> new DeliveryProduct(shoppingProduct.productId(), UUID.randomUUID(), shoppingProduct.amount(), shoppingProduct.amount() * shoppingProduct.price(), shoppingProduct.price()))
                .collect(Collectors.toSet());

        final List<NoSuchProductInStockException.Element> elements = deliveryProducts
                .stream()
                .filter(deliveryProduct -> !stockProductRepository.has(deliveryProduct.productId(), deliveryProduct.amount()))
                .map(deliveryProduct -> new NoSuchProductInStockException.Element(stockProductRepository, deliveryProduct.productId(), deliveryProduct.amount(), stockProductRepository.getAmount(deliveryProduct.productId())))
                .toList();

        if (!elements.isEmpty()) {
            throw new NoSuchProductInStockException(elements);
        }

        deliveryProducts.forEach(deliveryProduct -> stockProductRepository.removeAmount(deliveryProduct.productId(), deliveryProduct.amount()));

        final Order newOrder = new Order(
                orderId,
                deliveryProducts,
                customerId,
                postalAddress,
                creditCart
        );

        put(newOrder);

        return newOrder;
    }

    public void put(Order order) {
        Objects.requireNonNull(order);
        repository.put(order.orderId(), order);
        writeToFile();
    }

    public Order get(UUID orderId) {
        Objects.requireNonNull(orderId);
        return repository.get(orderId);
    }

    private void writeToFile() {
        json.encodeToFile(orderRepositoryFilePath, repository.values());
    }

    @Override
    public Iterator<Order> iterator() {
        return repository.values().iterator();
    }

    public static class NoSuchProductInStockException extends RuntimeException {

        private final List<Element> elements;

        public NoSuchProductInStockException(List<Element> elements) {
            this.elements = elements;
        }

        public List<Element> getElements() {
            return elements;
        }

        public record Element(StockProductRepository stockProductRepository, UUID productId, int desiredAmount,
                              int remainingAmount) {
        }
    }
}
