package fr.marketplace;

import javax.money.MonetaryAmount;
import java.util.Currency;

public class Utils {

    public static <T> T first(Iterable<T> iterable) {
        T value = null;
        for (T t : iterable) {
            value = t;
            break;
        }
        return value;
    }

    public static String prettyFormat(MonetaryAmount monetaryAmount) {
        final double value = monetaryAmount.getNumber().doubleValueExact();
        return value + " " + Currency.getInstance(monetaryAmount.getCurrency().getCurrencyCode()).getSymbol();
    }
}
