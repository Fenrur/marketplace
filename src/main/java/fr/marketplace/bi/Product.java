package fr.marketplace.bi;

import org.apache.commons.lang3.Validate;

import java.net.URI;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public record Product(UUID id, String name, String description, double price, Set<URI> images, boolean isAvailable) {

    public Product {
        Objects.requireNonNull(id);
        Validate.notBlank(name);
        Validate.notBlank(description);
        Validate.isTrue(price > 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return id.equals(product.id);
    }
}
