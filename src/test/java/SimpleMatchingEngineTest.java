import data.NewOrderImpl;
import data.Order;
import data.OrderSide;
import data.OrderType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleMatchingEngineTest {

    static AtomicInteger orderIdGenerator = new AtomicInteger(10000);

    private MatchingEngine engine;

    @BeforeEach
    public void setUp() {
        Map<String, Boolean> symbols = loadSymbols("symbols.csv");
        engine = new SimpleMatchingEngine(symbols.keySet());
    }

    @Test
    public void testAddNewOrderBuy() {
        Order order = new NewOrderImpl();
        order.setOrderId(1);
        order.setSymbol("AMZN");
        order.setOrderType(OrderType.LIMIT);
        order.setPrice(3170.95);
        order.setOrderSide(OrderSide.BUY);
        order.setOrderTime(1608917423.4089453);

        engine.newOrder(order);
        String expectedOrderBook = "Symbol: AMZN buy[3170.95] sell[]";
        Assertions.assertTrue(engine.getOrderBook(order.getSymbol()).toString().indexOf(expectedOrderBook) != -1);

        Order order2 = new NewOrderImpl();
        order.setOrderId(1);
        order.setSymbol("AMZN");
        order.setOrderType(OrderType.LIMIT);
        order.setPrice(3180.00);
        order.setOrderSide(OrderSide.BUY);
        order.setOrderTime(1608917502.4089453);

        engine.newOrder(order2);
        String expectedOrderBook2 = "Symbol: AMZN buy[3180.00|3170.95] sell[]";
        Assertions.assertTrue(engine.getOrderBook(order.getSymbol()).toString().indexOf(expectedOrderBook2) != -1);
    }

    @Test
    public void testAddNewOrderSell() {
        Order order = new NewOrderImpl();
        order.setOrderId(1);
        order.setSymbol("AMZN");
        order.setOrderType(OrderType.LIMIT);
        order.setPrice(3170.95);
        order.setOrderSide(OrderSide.SELL);
        order.setOrderTime(1608917423.4089453);

        engine.newOrder(order);
        String expectedOrderBook = "Symbol: AMZN buy[3170.95] sell[]";
        Assertions.assertTrue(engine.getOrderBook(order.getSymbol()).toString().indexOf(expectedOrderBook) != -1);

        Order order2 = new NewOrderImpl();
        order.setOrderId(1);
        order.setSymbol("AMZN");
        order.setOrderType(OrderType.LIMIT);
        order.setPrice(3180.00);
        order.setOrderSide(OrderSide.SELL);
        order.setOrderTime(1608917502.4089453);

        engine.newOrder(order2);
        String expectedOrderBook2 = "Symbol: AMZN buy[] sell[3170.95|3180.00]";
        Assertions.assertTrue(engine.getOrderBook(order.getSymbol()).toString().indexOf(expectedOrderBook2) != -1);
    }
    @Test
    public void testAddNewOrderHalted() {
        Order order = new NewOrderImpl();
        order.setOrderId(1);
        order.setSymbol("GOOG");
        order.setOrderType(OrderType.LIMIT);
        order.setPrice(3170.95);
        order.setOrderSide(OrderSide.SELL);
        order.setOrderTime(1608917423.4089453);

        engine.newOrder(order);
        Assertions.assertEquals(engine.getRejectOrders().size(), 1);
        Assertions.assertEquals(engine.getRejectOrders().get(0).getSymbol(), "GOOG");
    }
    @Test
    public void testLimitOrderMatchLimitOrder() {
        Order order = new NewOrderImpl();
        order.setOrderId(1);
        order.setSymbol("AMZN");
        order.setOrderType(OrderType.LIMIT);
        order.setPrice(3170.95);
        order.setOrderSide(OrderSide.SELL);
        order.setOrderTime(1608917423.4089453);

        engine.newOrder(order);
        String expectedOrderBook = "Symbol: AMZN buy[3170.95] sell[]";
        Assertions.assertTrue(engine.getOrderBook(order.getSymbol()).toString().indexOf(expectedOrderBook) != -1);

        Order order2 = new NewOrderImpl();
        order.setOrderId(1);
        order.setSymbol("AMZN");
        order.setOrderType(OrderType.LIMIT);
        order.setPrice(3180.00);
        order.setOrderSide(OrderSide.BUY);
        order.setOrderTime(1608917502.4089453);

        engine.newOrder(order2);
        Assertions.assertTrue(engine.getTrades().size() == 1);
        Assertions.assertEquals(engine.getTrades().get(0).getExecutionPrice(), 3170.95);
    }

    @Test
    public void testLimitOrderMatchMarketOrder() {
        Order order = new NewOrderImpl();
        order.setOrderId(1);
        order.setSymbol("AMZN");
        order.setOrderType(OrderType.LIMIT);
        order.setPrice(3170.95);
        order.setOrderSide(OrderSide.SELL);
        order.setOrderTime(1608917423.4089453);

        engine.newOrder(order);
        String expectedOrderBook = "Symbol: AMZN buy[3170.95] sell[]";
        Assertions.assertTrue(engine.getOrderBook(order.getSymbol()).toString().indexOf(expectedOrderBook) != -1);

        Order order2 = new NewOrderImpl();
        order.setOrderId(1);
        order.setSymbol("AMZN");
        order.setOrderType(OrderType.MARKET);
        order.setOrderSide(OrderSide.BUY);
        order.setOrderTime(1608917502.4089453);

        engine.newOrder(order2);
        Assertions.assertTrue(engine.getTrades().size() == 1);
        Assertions.assertEquals(engine.getTrades().get(0).getExecutionPrice(), 3170.95);
    }

    public List<Order> loadOrders(String fileName) throws Exception {
        List<Order> orders = new ArrayList<>();
        InputStream is = getFileFromResourceAsStream(fileName);
        try (InputStreamReader streamReader =
                     new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                orders.add(getOrderFromLine(line));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return orders;
    }

    private Order getOrderFromLine(String line) throws Exception {
        String[] tokens = line.split(",");
        String symbol = tokens[0];
        String side = tokens[1];
        String type = tokens[2];
        String price = tokens[3];
        String timestamp = tokens[4];

        Order order = new NewOrderImpl();
        order.setSymbol(symbol);
        order.setOrderId(orderIdGenerator.incrementAndGet());
        order.setOrderSide(OrderSide.fromName(side).get());
        order.setOrderType(OrderType.fromName(type).get());
        if(price != null && !"".equals(price)) {
            order.setPrice(Double.parseDouble(price));
        }
        order.setOrderTime(Double.parseDouble(timestamp));
        return order;
    }

    private InputStream getFileFromResourceAsStream(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }

    public Map<String, Boolean> loadSymbols(String fileName) {
        Map<String, Boolean> symbolMap = new HashMap<>();
        InputStream is = getFileFromResourceAsStream(fileName);
        try (InputStreamReader streamReader =
                     new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                symbolMap.put(tokens[0], Boolean.valueOf(tokens[1]));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return symbolMap;
    }

    public static void main(String[] args) {

        SimpleMatchingEngineTest test = new SimpleMatchingEngineTest();
        try {
            List<Order> orders = test.loadOrders("orders.csv");
            Map<String, Boolean> symbols = test.loadSymbols("symbols.csv");

            MatchingEngine engine = new SimpleMatchingEngine(symbols.keySet());
            for(String symbol : symbols.keySet()) {
                if(symbols.get(symbol)) {
                    engine.haltSymbol(symbol);
                }
            }

            for(Order order : orders) {
                try {
                    engine.newOrder(order);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

            EngineVisitor visitor = new EngineResultPersistVisitor();
            engine.acceptVisitor(visitor);

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
