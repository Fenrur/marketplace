package fr.marketplace.bi;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public record Order(UUID orderId, Set<DeliveryProduct> deliveryProducts, UUID customerId, PostalAddress postalAddress,
                    CreditCart creditCart) {

    public Order(UUID orderId, Set<DeliveryProduct> deliveryProducts, UUID customerId, PostalAddress postalAddress, CreditCart creditCart) {
        Objects.requireNonNull(orderId);
        Objects.requireNonNull(deliveryProducts);
        Objects.requireNonNull(customerId);
        Objects.requireNonNull(creditCart);

        if (deliveryProducts.isEmpty()) {
            throw new RuntimeException("Must minimum 1 product");
        }

        this.orderId = orderId;
        this.deliveryProducts = Set.copyOf(deliveryProducts);
        this.customerId = customerId;
        this.postalAddress = postalAddress;
        this.creditCart = creditCart;
    }

    public double totalPrice() {
        return deliveryProducts
                .stream()
                .mapToDouble(DeliveryProduct::totalPrice)
                .sum();
    }
}
