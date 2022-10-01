package fr.marketplace.bi;

import java.util.UUID;

record DeliveryInformation(UUID productId, UUID orderId, User deliveryUserId, State state) {

    public enum State {
        PRODUCT_DELIVERED,
        PRODUCT_IN_PREPARATION_FOR_SHIPMENT,
        PRODUCT_SHIPPED
    }
}
