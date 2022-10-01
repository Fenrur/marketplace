package fr.marketplace.bi;

import org.apache.commons.lang3.Validate;

import java.time.MonthDay;
import java.time.Year;
import java.util.Objects;

public record CreditCart(String nameOnCart, int number, MonthDay monthDay, Year year) {

    public CreditCart {
        Validate.notBlank(nameOnCart);
        if (!isValidCart(number)) throw new NotValidCartNumber();
        Objects.requireNonNull(monthDay);
        Objects.requireNonNull(year);
    }

    public static boolean isValidCart(int number) {
        return true;
    }

    public static class NotValidCartNumber extends RuntimeException {

    }
}
