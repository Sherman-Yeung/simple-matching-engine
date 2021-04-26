package data;

public class OrderValidatorImpl implements OrderValidator {

    public void validate(Order order) throws Exception {

        if(order.getOrderId() == -1) {
            throw new Exception("OrderId is required.");
        }
        if(order.getSymbol() == null || order.getSymbol().trim().equals("")) {
            throw new Exception("Symbol is required.");
        }
        if(order.getOrderSide() == null) {
            throw new Exception("OrderSide is required.");
        }
        if(order.getOrderType() == null) {
            throw new Exception("OrderType is required.");
        }
    }

}
