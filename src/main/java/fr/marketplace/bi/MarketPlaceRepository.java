package fr.marketplace.bi;

public record MarketPlaceRepository(ProductRepository productRepository, OrderRepository orderRepository,
                                    UserRepository userRepository, StockProductRepository stockProductRepository,
                                    DeliveryRepository deliveryRepository) {
}
