package data;

import java.util.Date;

/**
 * data.Order is interface for data.Order class implementation
 *
 * @author Shuyuk Yeung
 *
 */
public interface Order {

    int getOrderId();

    double getOrderTime();

    String getSymbol();

    double getPrice();

    OrderSide getOrderSide();

    OrderType getOrderType();

    String getText();

    void setOrderId(int orderId);

    void setOrderTime(double orderTime);

    void setSymbol(String symbol);

    void setPrice(double price);

    void setOrderSide(OrderSide orderSide);

    void setOrderType(OrderType orderType);

    void setText(String text);
}