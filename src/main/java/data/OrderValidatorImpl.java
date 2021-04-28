package data;

public class OrderValidatorImpl implements OrderValidator {

    public static final String ORDER_ID_REQUIRED = "OrderId is required.";
    public static final String SYMBOL_REQUIRED = "Symbol is required.";
    public static final String ORDER_SIDE_REQUIRED = "OrderSide is required.";
    public static final String ORDER_TYPE_REQUIRED = "OrderType is required.";

    public void validate(Order order) throws Exception {

        if(order.getOrderId() == -1) {
            throw new Exception(ORDER_ID_REQUIRED);
        }
        if(order.getSymbol() == null || order.getSymbol().trim().equals("")) {
            throw new Exception(SYMBOL_REQUIRED);
        }
        if(order.getOrderSide() == null) {
            throw new Exception(ORDER_SIDE_REQUIRED);
        }
        if(order.getOrderType() == null) {
            throw new Exception(ORDER_TYPE_REQUIRED);
        }
    }

}
