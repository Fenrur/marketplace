package fr.marketplace.bi;

public record MarketPlaceRepository(ProductRepository productRepository, OrderRepository orderRepository,
                                    UserRepository userRepository, StockProductRepository stockProductRepository,
                                    DeliveryServiceRepository deliveryServiceRepository) {
}
