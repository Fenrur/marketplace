package fr.marketplace.bi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.Validate;

import javax.money.MonetaryAmount;
import java.net.URI;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public record Product(UUID id, String name, String description, MonetaryAmount price, Set<URI> images,
                      boolean isAvailable, boolean isAvailableByMarketplace) {

    public Product {
        Objects.requireNonNull(id);
        Validate.notBlank(name, "Product name is empty");
        Validate.notBlank(description, "Description is empty");
        Objects.requireNonNull(price);
        Validate.isTrue(price.isPositive(), "Price must be positive");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @JsonIgnore
    public boolean productIsAvailable() {
        return isAvailableByMarketplace && isAvailable;
    }
}
