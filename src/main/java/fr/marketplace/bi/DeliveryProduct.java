package fr.marketplace.bi;

import org.apache.commons.lang3.Validate;

import javax.money.MonetaryAmount;
import java.util.Objects;
import java.util.UUID;

public record DeliveryProduct(UUID productId, UUID deliveryProductId, int amount, MonetaryAmount unitPrice) {

    public DeliveryProduct {
        Objects.requireNonNull(productId);
        Objects.requireNonNull(deliveryProductId);
        Validate.isTrue(amount > 0);
        Objects.requireNonNull(unitPrice);
        Validate.isTrue(unitPrice.isPositive());
    }

    public MonetaryAmount totalPrice() {
        return unitPrice.multiply(amount);
    }
}
