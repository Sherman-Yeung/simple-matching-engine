package data;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class TradeExecution {
    //Trade ID generator, better move to it's own interface and implementation
    static AtomicInteger execIdGenerator = new AtomicInteger(20000);

    int _tradeId;
    String _symbol;
    double _executionPrice;
    Date _executionTime;

    public TradeExecution(String symbol, double executionPrice, Date executionTime) {
        this._tradeId = execIdGenerator.incrementAndGet();
        this._symbol = symbol;
        this._executionPrice = executionPrice;
        this._executionTime = executionTime;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TradeId: ").append(_tradeId).append(", ");
        sb.append("Symbol: ").append(_symbol).append(", ");
        sb.append("ExecutionPrice: ").append(_executionPrice).append(", ");
        sb.append("ExecutionTime: ").append(_executionTime);
        return sb.toString();
    }

    public int getTradeId() {
        return _tradeId;
    }

    public String getSymbol() {
        return _symbol;
    }

    public double getExecutionPrice() {
        return _executionPrice;
    }

    public Date getExecutionTime() {
        return _executionTime;
    }
}
