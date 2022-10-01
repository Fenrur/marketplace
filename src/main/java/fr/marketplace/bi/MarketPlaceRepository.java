package fr.marketplace.bi;

public record MarketPlaceRepository(ProductRepository repository, OrderRepository orderRepository,
                                    UserRepository userRepository, StockProductRepository stockProductRepository,
                                    DeliveryServiceRepository deliveryServiceRepository) {
}
