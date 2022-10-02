package fr.marketplace.controller;

import fr.marketplace.bi.ShoppingProduct;
import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.api.factory.Bags;

import javax.money.MonetaryAmount;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ShoppingCart {

    private final MutableBag<Key> cart = Bags.mutable.empty();

    public void add(UUID productId, MonetaryAmount price, int amount) {
        cart.addOccurrences(new Key(productId, price), amount);
    }

    public void remove(UUID productId, MonetaryAmount price, int amount) {
        cart.removeOccurrences(new Key(productId, price), amount);
    }

    public final List<ShoppingProduct> getShoppingProducts() {
        return Collections.unmodifiableList(
                cart
                        .collectWithOccurrences((key, occurrence) -> new ShoppingProduct(key.productId, key.price, occurrence))
                        .toList()
        );
    }

    private record Key(UUID productId, MonetaryAmount price) {

    }
}
