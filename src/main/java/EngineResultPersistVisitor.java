import data.Order;
import data.TradeExecution;

import java.io.FileWriter;
import java.util.List;

public class EngineResultPersistVisitor implements EngineVisitor {

    public static final String TRADE_FILE_NAME = "trades.txt";
    public static final String REJECT_ORDER_FILE_NAME = "rejected.txt";
    public static final String ORDERBOOK_FILE_NAME = "orderbook.txt";

    @Override
    public void visit(MatchingEngine engine) {
        List<TradeExecution> tradeList = engine.getTrades();
        persistTrades(tradeList);

        List<Order> rejectOrders = engine.getRejectOrders();
        persistRejectOrders(rejectOrders);

        List<OrderBook> orderBooks = engine.getOrderBooks();
        persistOrderBooks(orderBooks);
    }

    private void persistTrades(List<TradeExecution> tradeList) {
        try {
            FileWriter myWriter = new FileWriter(TRADE_FILE_NAME);
            for(TradeExecution trade : tradeList) {
                myWriter.write(trade.toString()+"\n");
            }
            myWriter.flush();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void persistRejectOrders(List<Order> orders) {
        try {
            FileWriter myWriter = new FileWriter(REJECT_ORDER_FILE_NAME);
            for(Order order : orders) {
                myWriter.write(order.toString()+"\n");
            }
            myWriter.flush();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void persistOrderBooks(List<OrderBook> orderBooks) {
        try {
            FileWriter myWriter = new FileWriter(ORDERBOOK_FILE_NAME);
            for(OrderBook orderBook : orderBooks) {
                myWriter.write(orderBook.toString()+"\n");
            }
            myWriter.flush();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
