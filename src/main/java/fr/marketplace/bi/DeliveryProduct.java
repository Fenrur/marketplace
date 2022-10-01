package fr.marketplace.bi;

import org.apache.commons.lang3.Validate;

import java.util.Objects;
import java.util.UUID;

public record DeliveryProduct(UUID productId, UUID deliveryProductId, int amount, double totalPrice, double price) {

    public DeliveryProduct {
        Objects.requireNonNull(productId);
        Objects.requireNonNull(deliveryProductId);
        Validate.isTrue(amount > 0);
        Validate.isTrue(totalPrice > 0);
        Validate.isTrue(price > 0);
    }
}
