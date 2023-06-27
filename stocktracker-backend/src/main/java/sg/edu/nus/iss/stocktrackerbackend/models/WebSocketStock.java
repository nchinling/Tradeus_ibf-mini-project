package sg.edu.nus.iss.stocktrackerbackend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WebSocketStock {
    private String symbol;
    private String exchange;
    private String currency;
    private Double price;
    private Double ask;
    private Double day_volume;
    private Double bid;
    public WebSocketStock() {
    }

    

    public WebSocketStock(String symbol, String exchange, String currency, Double price, Double ask, Double day_volume,
            Double bid) {
        this.symbol = symbol;
        this.exchange = exchange;
        this.currency = currency;
        this.price = price;
        this.ask = ask;
        this.day_volume = day_volume;
        this.bid = bid;
    }



    public String getSymbol() {
        return symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public String getExchange() {
        return exchange;
    }
    public void setExchange(String exchange) {
        this.exchange = exchange;
    }
    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
   

    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public Double getAsk() {
        return ask;
    }
    public void setAsk(Double ask) {
        this.ask = ask;
    }
    public Double getDay_volume() {
        return day_volume;
    }
    public void setDay_volume(Double day_volume) {
        this.day_volume = day_volume;
    }
    public Double getBid() {
        return bid;
    }
    public void setBid(Double bid) {
        this.bid = bid;
    }

    @Override
    public String toString() {
        return "WebSocketStock [symbol=" + symbol + ", exchange=" + exchange + ", currency=" + currency + ", price="
                + price + ", ask=" + ask + ", day_volume=" + day_volume + ", bid=" + bid + "]";
    }

    
   

    

    
 
}
