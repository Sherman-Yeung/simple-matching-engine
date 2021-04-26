import data.Order;
import data.TradeExection;

import java.util.List;

public interface MatchingEngine {

    void newOrder(Order order);
    void amendOrder(Order order, int oldOrderId);
    void cancelOrder(Order order);
    void haltSymbol(String symbol);
    void activateSymbol(String symbol);

    List<OrderBook> getOrderBooks();
    OrderBook getOrderBook(String symbol);
    List<Order> getRejectOrders();
    List<TradeExection> getTrades();

    void acceptVisitor(EngineVisitor visitor);
}
