import data.Order;
import data.OrderSide;
import data.OrderType;
import data.OrderValidator;
import data.OrderValidatorImpl;
import data.TradeExection;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * SimpleMatchingEngine that match buy and sell order (assume order quantity always equals)
 *
 * @author Shuyuk Yeung
 *
 */

public class SimpleMatchingEngine implements MatchingEngine {

    private Map<String, OrderBook> _orderBooks = new HashMap<>();
    private Set<String> _haltedSymbols = new HashSet<>();

    private List<Order> _rejectOrders = new ArrayList<>();
    private List<TradeExection> _tradeExectionList = new ArrayList<>();

    private OrderValidator _validator = new OrderValidatorImpl();

    public SimpleMatchingEngine(Set<String> symbols) {
        for(String symbol : symbols) {
            _orderBooks.put(symbol, new OrderBook(symbol));
        }
    }

    public void newOrder(Order order) {
        System.out.println("Received new order: " + order);
        try {
            validate(order);
            OrderBook orderBook = _orderBooks.get(order.getSymbol());
            orderBook.addNewOrder(order);
            tryMatch(order, orderBook);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void amendOrder(Order order, int oldOrderId) {
        System.out.println("Received amend order: " + order + ", oldOrderId: " + oldOrderId);
        try {
            validate(order);
            OrderBook orderBook = _orderBooks.get(order.getSymbol());
            orderBook.replaceOrder(order, oldOrderId);
            tryMatch(order, orderBook);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void cancelOrder(Order order) {
        System.out.println("Received cancel order: " + order.getOrderId());
        try {
            OrderBook orderBook = _orderBooks.get(order.getSymbol());
            orderBook.removeOrder(order.getOrderId());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void tryMatch(Order order, OrderBook orderBook) throws Exception {
        OrderType type = order.getOrderType();
        Order buyOrder, sellOrder;
        switch (type) {
            case LIMIT:
                buyOrder = orderBook._buyOrderQueue.peek();
                sellOrder = orderBook._sellOrderQueue.peek();
                if(buyOrder != null && sellOrder != null && buyOrder.getPrice() >= sellOrder.getPrice()) {
                    buyOrder = orderBook._buyOrderQueue.remove();
                    sellOrder = orderBook._sellOrderQueue.remove();
                    generateTrade(order.getSymbol(), buyOrder.getPrice());
                }
                break;
            case MARKET:
                OrderSide side = order.getOrderSide();
                switch (side) {
                    case BUY:
                        if(!orderBook._sellOrderQueue.isEmpty()) {
                            sellOrder = orderBook._sellOrderQueue.remove();
                            generateTrade(order.getSymbol(), sellOrder.getPrice());
                        }
                        break;
                    case SELL:
                        if(!orderBook._buyOrderQueue.isEmpty()) {
                            buyOrder = orderBook._buyOrderQueue.remove();
                            generateTrade(order.getSymbol(), buyOrder.getPrice());
                        }
                        break;
                    default:
                }
            default:
        }
    }

    private void generateTrade(String symbol, double price) {
        TradeExection newTrade = new TradeExection(symbol, price, new Date());
        System.out.println(newTrade);
        _tradeExectionList.add(newTrade);
    }

    private void validate(Order order) throws Exception {
        if(_haltedSymbols.contains(order.getSymbol())) {
            order.setText("Symbol halted!");
            _rejectOrders.add(order);
            throw new Exception("Symbol halted!");
        }
        if(!_orderBooks.containsKey(order.getSymbol())) {
            order.setText("Symbol not supported!");
            _rejectOrders.add(order);
            throw new Exception("Symbol not supported!");
        }
        if(_validator != null) {
            try {
                _validator.validate(order);
            }catch (Exception e) {
                order.setText(e.getMessage());
                _rejectOrders.add(order);
                throw e;
            }
        }
    }

    public void setValidator(OrderValidator validator) {
        _validator = validator;
    }

    public void haltSymbol(String symbol) {
        _haltedSymbols.add(symbol);
    }

    public void activateSymbol(String symbol) {
        _haltedSymbols.remove(symbol);
    }

    public void acceptVisitor(EngineVisitor visitor) {
        visitor.visit(this);
    }
    public List<OrderBook> getOrderBooks() {
        return new ArrayList<OrderBook>(_orderBooks.values());
    }
    public OrderBook getOrderBook(String symbol) {
        return _orderBooks.get(symbol);
    }
    public List<Order> getRejectOrders(){
        return _rejectOrders;
    }
    public List<TradeExection> getTrades() {
        return _tradeExectionList;
    }
}