package fr.marketplace;

import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.GridPane;
import org.javamoney.moneta.FastMoney;

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
        GridPane pane = new GridPane();

        final double value = monetaryAmount.getNumber().doubleValueExact();
        return value + " " + Currency.getInstance(monetaryAmount.getCurrency().getCurrencyCode()).getSymbol();
    }
}
