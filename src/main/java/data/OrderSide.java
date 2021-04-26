package data;

import java.util.Arrays;
import java.util.Optional;

public enum OrderSide {
    BUY("Buy"), SELL("Sell");

    OrderSide(String name) {
        this.name = name;
    }

    private String name;
    public static Optional<OrderSide> fromName(String name) {
        return Arrays.stream(values())
                .filter(bl -> bl.name.equalsIgnoreCase(name))
                .findFirst();
    }
}
