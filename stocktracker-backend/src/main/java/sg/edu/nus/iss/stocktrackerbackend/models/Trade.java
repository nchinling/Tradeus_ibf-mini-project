package sg.edu.nus.iss.stocktrackerbackend.models;

import java.time.LocalDate;

public class Trade {

    private String accountId;
    private String username;
    private String exchange;
    private String stockName;
    private String symbol;
    private Double units;
    private Double price;
    private Double fee;
    private LocalDate date;
    public Trade() {
    }
    

    public Trade(String accountId, String username, String exchange, String stockName, String symbol, Double units,
            Double price, Double fee, LocalDate date) {
        this.accountId = accountId;
        this.username = username;
        this.exchange = exchange;
        this.stockName = stockName;
        this.symbol = symbol;
        this.units = units;
        this.price = price;
        this.fee = fee;
        this.date = date;
    }


    public String getAccountId() {
        return accountId;
    }
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getExchange() {
        return exchange;
    }
    public void setExchange(String exchange) {
        this.exchange = exchange;
    }
    public String getStockName() {
        return stockName;
    }
    public void setStockName(String stockName) {
        this.stockName = stockName;
    }
    public String getSymbol() {
        return symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public Double getUnits() {
        return units;
    }
    public void setUnits(Double units) {
        this.units = units;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public Double getFee() {
        return fee;
    }
    public void setFee(Double fee) {
        this.fee = fee;
    }


    @Override
    public String toString() {
        return "Trade [accountId=" + accountId + ", username=" + username + ", exchange=" + exchange + ", stockName="
                + stockName + ", symbol=" + symbol + ", units=" + units + ", price=" + price + ", fee=" + fee
                + ", date=" + date + "]";
    }

    
    

    
}


