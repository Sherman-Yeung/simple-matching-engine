import data.Order;
import data.OrderSide;
import data.OrderType;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * OrderBook for a symbol (instrument)
 *
 * @author Shuyuk Yeung
 *
 */
public class OrderBook {

    String _ticker;
    PriorityQueue<Order> _buyOrderQueue;
    PriorityQueue<Order> _sellOrderQueue;

    Map<Integer, Order> _orderMap = new HashMap<>();

    public OrderBook(String ticker) {
        this._ticker = ticker;
        //Sort buy order by price (highest) then timestamp (earliest)
        _buyOrderQueue = new PriorityQueue<Order>(new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                int priceCheck = Double.compare(o1.getPrice(), o2.getPrice());
                if(priceCheck != 0) {
                    return priceCheck * -1;
                }else{
                    return Double.compare(o1.getOrderTime(),o2.getOrderTime());
                }
            }
        });
        //Sort sell order by price (lowest) then timestamp (earliest)
        _sellOrderQueue = new PriorityQueue<Order>(new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                int priceCheck = Double.compare(o1.getPrice(), o2.getPrice());
                if(priceCheck != 0) {
                    return priceCheck;
                }else{
                    return Double.compare(o1.getOrderTime(),o2.getOrderTime());
                }
            }
        });
    }

    public void addNewOrder(Order order) throws Exception {
        if(_orderMap.containsKey(order.getOrderId())) {
            throw new Exception("Duplicate order: " + order.getOrderId());
        }
        _orderMap.put(order.getOrderId(), order);
        if(order.getOrderType() == OrderType.LIMIT) {
            if(order.getOrderSide() == OrderSide.BUY) {
                _buyOrderQueue.add(order);
            }else{
                _sellOrderQueue.add(order);
            }
        }
    }

    public void removeOrder(int orderId) throws Exception {
        Order order = _orderMap.get(orderId);
        if(order  == null) {
            throw new Exception("data.Order not found: " + orderId);
        }
        if(order.getOrderSide() == OrderSide.BUY) {
            _buyOrderQueue.remove(order);
        }else{
            _sellOrderQueue.remove(order);
        }
    }

    public void replaceOrder(Order order, int oldOrderId) throws Exception {
        removeOrder(oldOrderId);
        addNewOrder(order);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Symbol: ").append(_ticker);
        if(!_buyOrderQueue.isEmpty()) {
            sb.append(" buy[");
            PriorityQueue<Order> tmpBuyQueue = new PriorityQueue<>(_buyOrderQueue);
            for (Order order : tmpBuyQueue) {
                sb.append(order.getPrice()).append("|");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("]");
        }else{
            sb.append(" buy[] ");
        }
        if(!_sellOrderQueue.isEmpty()) {
            sb.append(" sell[");
            PriorityQueue<Order> tmpSellQueue = new PriorityQueue<>(_sellOrderQueue);
            for (Order order : tmpSellQueue) {
                sb.append(order.getPrice()).append("|");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("]");
        }else{
            sb.append(" sell[] ");
        }
        return sb.toString();
    }

}