package fr.marketplace.controller;

import java.util.Currency;
import java.util.Set;
import java.util.stream.Collectors;

public record PrettyCurrency(Currency currency) {

    private static final Set<PrettyCurrency> INSTANCES = Currency
            .getAvailableCurrencies()
            .stream()
            .map(PrettyCurrency::new)
            .collect(Collectors.toUnmodifiableSet());

    @Override
    public String toString() {
        return currency.getSymbol();
    }

    public static Set<PrettyCurrency> getAvailableCurrencies() {
        return INSTANCES;
    }

    public static PrettyCurrency defaultPrettyCurrency() {
        return new PrettyCurrency(Currency.getInstance("EUR"));
    }
}
