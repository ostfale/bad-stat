package de.ostfale.qk.db.internal.match;

import java.util.Arrays;
import java.util.Optional;

public enum MatchOrder {

    ROUND_OF_128("Round of 128", 128),
    ROUND_OF_64("Round of 64", 64),
    ROUND_OF_32("Round of 32", 32),
    ROUND_OF_16("Round of 16", 16),
    ROUND_OF_8("Round of 8", 8),
    ROUND_OF_4("Quarter final", 4),
    ROUND_OF_2("Semi final", 2),
    ROUND_THIRD_PLACE("3rd/4th place", 3),
    ROUND_OF_1("Final", 1);

    private final String displayName;
    private final Integer order;

    public static Integer lookupOrder(String aDisplayName) {
       Optional<MatchOrder> optOrder= Arrays.stream(MatchOrder.values())
               .filter(order -> order.displayName.equalsIgnoreCase(aDisplayName)).findFirst();
        return optOrder.map(order->order.order).orElse(0);
    }

    MatchOrder(String aDisplayName, Integer aOrder) {
        this.displayName = aDisplayName;
        this.order = aOrder;
    }
}
