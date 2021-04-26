package data;

import java.util.Arrays;
import java.util.Optional;

public enum OrderType {
    LIMIT("Limit"), MARKET("Market");

    OrderType(String name) {
        this.name = name;
    }

    private String name;
    public static Optional<OrderType> fromName(String name) {
        return Arrays.stream(values())
                .filter(bl -> bl.name.equalsIgnoreCase(name))
                .findFirst();
    }
}
