package fr.marketplace.bi;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.javamoney.moneta.FastMoney;

import javax.money.MonetaryAmount;
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

    record ByPriceMax(double max) implements Search {

        public ByPriceMax {
            Validate.isTrue(max >= 0, "Max need to be equal to 0 or more");
        }

        @Override
        public void accept(Set<Product> actualSearches) {
            actualSearches.removeIf(product -> {
                final MonetaryAmount productPrice = product.price();
                final FastMoney max = FastMoney.of(this.max, productPrice.getCurrency());

                return !productPrice.isLessThanOrEqualTo(max);
            });
        }
    }

    record ByPriceMin(double min) implements Search {

        public ByPriceMin {
            Validate.isTrue(min >= 0, "Min need to be equal to 0 or more");
        }

        @Override
        public void accept(Set<Product> actualSearches) {
            actualSearches.removeIf(product -> {
                final MonetaryAmount productPrice = product.price();
                final FastMoney min = FastMoney.of(this.min, productPrice.getCurrency());
                return !productPrice.isGreaterThanOrEqualTo(min);
            });
        }
    }
}
