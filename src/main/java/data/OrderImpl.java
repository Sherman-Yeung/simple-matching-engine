package data;

import java.util.Date;

/**
 * This data.OrderImpl class represents blueprint for user requested orders
 *
 * @author Shuyuk Yeung
 *
 */
public class OrderImpl implements Order {

    int _orderId = -1;
    double _orderTime;
    String _symbol;
    double _price;
    OrderSide _orderSide;
    OrderType _orderType;
    String _text;

    public OrderImpl(int _orderId, double _orderTime, String symbol, double price, OrderSide orderSide, OrderType orderType) {
        this._orderId = _orderId;
        this._orderTime = _orderTime;
        this._symbol = symbol;
        this._price = price;
        this._orderSide = orderSide;
        this._orderType = orderType;
    }

    public OrderImpl(){}

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OrderId: ").append(_orderId).append(", ");
        sb.append("Symbol: ").append(_symbol).append(", ");
        sb.append("Side: ").append(_orderSide).append(", ");
        sb.append("Type: ").append(_orderType).append(", ");
        sb.append("Price: ").append(_price).append(", ");
        sb.append("Tmestamp: ").append(_orderTime).append(", ");
        if(_text!=null) {
            sb.append("Error: ").append(_text);
        }else{
            sb.deleteCharAt(sb.length()-2);
        }
        return sb.toString();
    }


    public int getOrderId() {
        return _orderId;
    }

    public double getOrderTime() {
        return _orderTime;
    }

    public String getSymbol() {
        return _symbol;
    }

    public double getPrice() {
        return _price;
    }

    public OrderSide getOrderSide() {
        return _orderSide;
    }

    public OrderType getOrderType() {
        return _orderType;
    }

    public String getText() {
        return _text;
    }

    public void setOrderId(int orderId) {
        this._orderId = orderId;
    }

    public void setOrderTime(double orderTime) {
        this._orderTime = orderTime;
    }

    public void setSymbol(String symbol) {
        this._symbol = symbol;
    }

    public void setPrice(double price) {
        this._price = price;
    }

    public void setOrderSide(OrderSide orderSide) {
        this._orderSide = orderSide;
    }

    public void setOrderType(OrderType orderType) {
        this._orderType = orderType;
    }

    public void setText(String _text) {
        this._text = _text;
    }
}