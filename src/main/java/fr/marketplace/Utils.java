package fr.marketplace;

public class Utils {

    public static <T> T first(Iterable<T> iterable) {
        T value = null;
        for (T t : iterable) {
            value = t;
            break;
        }
        return value;
    }
}
