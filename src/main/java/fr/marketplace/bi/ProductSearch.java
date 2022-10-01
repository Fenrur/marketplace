package fr.marketplace.bi;

import java.util.List;
import java.util.Objects;

public record ProductSearch(List<Search> searches) {

    public ProductSearch(List<Search> searches) {
        Objects.requireNonNull(searches);
        this.searches = List.copyOf(searches);
    }

}
