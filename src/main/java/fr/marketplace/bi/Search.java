package fr.marketplace.bi;

import org.apache.commons.lang3.StringUtils;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public sealed interface Search extends Consumer<Set<Product>> {

    record ByName(String searchedName) implements Search {
        public ByName {
            Objects.requireNonNull(searchedName);
        }

        @Override
        public void accept(Set<Product> actualSearches) {
            actualSearches.removeIf(product -> !StringUtils.containsIgnoreCase(product.name(), searchedName));
        }
    }

    record ByPriceBetween(double min, double max) implements Search {
        public ByPriceBetween {
            if (min > max) throw new IllegalArgumentException("max must be > or = than min");
        }

        @Override
        public void accept(Set<Product> actualSearches) {
            actualSearches.removeIf(product -> min <= product.price() && product.price() <= max);
        }
    }

    record ByMaxZonedDateTime(ZonedDateTime max) implements Search {
        public ByMaxZonedDateTime {
            Objects.requireNonNull(max);
        }

        @Override
        public void accept(Set<Product> actualSearches) {
            actualSearches.removeIf(product -> product.estimatedDelivery().isBefore(max));
        }
    }
}
