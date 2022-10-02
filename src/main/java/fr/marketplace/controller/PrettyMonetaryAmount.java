package fr.marketplace.controller;

import fr.marketplace.Utils;

import javax.money.MonetaryAmount;

public record PrettyMonetaryAmount(MonetaryAmount monetaryAmount) implements Comparable<MonetaryAmount> {

    @Override
    public int compareTo(MonetaryAmount o) {
        return monetaryAmount.compareTo(o);
    }

    @Override
    public String toString() {
        return Utils.prettyFormat(monetaryAmount);
    }
}
