package fr.marketplace.bi;

import javax.money.MonetaryAmount;
import java.util.UUID;

public record ShoppingProduct(UUID productId, MonetaryAmount unitPrice, int amount) {

}
