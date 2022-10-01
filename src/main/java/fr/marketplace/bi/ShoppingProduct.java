package fr.marketplace.bi;

import java.util.UUID;

public record ShoppingProduct(UUID productId, double price, int amount) {

}
