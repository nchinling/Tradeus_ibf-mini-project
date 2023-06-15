package sg.edu.nus.iss.stocktrackerbackend.models;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Stock {
    
    //general stock data
    private String symbol;
    private String name;
    private String exchange;
    private String currency;

    //date and time
    private String datetime;
    private long timestamp; 

    //price data
    private double open;
    private double high;
    private double low;
    private double close;
    private long volume;
    private double previousClose;
    private double change;
    private double percentChange;
    private long averageVolume;
    
    public Stock() {
    }
    public Stock(String symbol, String name, String exchange, String currency, String datetime, long timestamp,
            double open, double high, double low, double close, long volume, double previousClose, double change,
            double percentChange, long averageVolume) {
        this.symbol = symbol;
        this.name = name;
        this.exchange = exchange;
        this.currency = currency;
        this.datetime = datetime;
        this.timestamp = timestamp;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.previousClose = previousClose;
        this.change = change;
        this.percentChange = percentChange;
        this.averageVolume = averageVolume;
    }
    public String getSymbol() {
        return symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
    public String getDatetime() {
        return datetime;
    }
    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public double getOpen() {
        return open;
    }
    public void setOpen(double open) {
        this.open = open;
    }
    public double getHigh() {
        return high;
    }
    public void setHigh(double high) {
        this.high = high;
    }
    public double getLow() {
        return low;
    }
    public void setLow(double low) {
        this.low = low;
    }
    public double getClose() {
        return close;
    }
    public void setClose(double close) {
        this.close = close;
    }
    public long getVolume() {
        return volume;
    }
    public void setVolume(long volume) {
        this.volume = volume;
    }
    public double getPreviousClose() {
        return previousClose;
    }
    public void setPreviousClose(double previousClose) {
        this.previousClose = previousClose;
    }
    public double getChange() {
        return change;
    }
    public void setChange(double change) {
        this.change = change;
    }
    public double getPercentChange() {
        return percentChange;
    }
    public void setPercentChange(double percentChange) {
        this.percentChange = percentChange;
    }
    public long getAverageVolume() {
        return averageVolume;
    }
    public void setAverageVolume(long averageVolume) {
        this.averageVolume = averageVolume;
    }
    @Override
    public String toString() {
        return "Stock [symbol=" + symbol + ", name=" + name + ", exchange=" + exchange + ", currency=" + currency
                + ", datetime=" + datetime + ", timestamp=" + timestamp + ", open=" + open + ", high=" + high + ", low="
                + low + ", close=" + close + ", volume=" + volume + ", previousClose=" + previousClose + ", change="
                + change + ", percentChange=" + percentChange + ", averageVolume=" + averageVolume + "]";
    }


    
    public static Stock createUserObject(String json) throws IOException {
        Stock s = new Stock();
        try(InputStream is = new ByteArrayInputStream(json.getBytes())){
            JsonReader r = Json.createReader(is);
            JsonObject o = r.readObject();
            s.setSymbol(o.getString("symbol"));
            s.setName(o.getString("name"));
            s.setExchange(o.getString("exchange"));
            s.setCurrency(o.getString("currency"));
            s.setOpen(Double.parseDouble(o.getString("open")));
            s.setHigh(Double.parseDouble(o.getString("high")));
            s.setLow(Double.parseDouble(o.getString("low")));
            s.setClose(Double.parseDouble(o.getString("close")));
            s.setVolume(Long.parseLong(o.getString("volume")));
            s.setChange(Double.parseDouble(o.getString("change")));
            s.setPercentChange(Double.parseDouble(o.getString("percent_change")));
            s.setDatetime(o.getString("datetime"));
        }
        return s;
    }

    public JsonObject toJSON(){
    
        return Json.createObjectBuilder()
            .add("symbol", this.getSymbol())
            .add("name", this.getName())
            .add("exchange", this.getExchange())
            .add("currency", this.getCurrency())
            .add("open", this.getOpen())
            .add("high", this.getHigh())
            .add("low", this.getLow())
            .add("close", this.getClose())
            .add("volume", this.getVolume())
            .add("change", this.getChange())
            .add("percent_change", this.getPercentChange())
            .add("datetime", this.getDatetime())
            .build();

            
    }


    public static Stock createUserObjectFromRedis(String jsonStr) throws IOException{
    Stock s = new Stock();
        try(InputStream is = new ByteArrayInputStream(jsonStr.getBytes())) {
            JsonObject o = toJSON(jsonStr);
             s.setSymbol(o.getString("symbol"));
            s.setName(o.getString("name"));
            s.setExchange(o.getString("exchange"));
            s.setCurrency(o.getString("currency"));
            s.setOpen(o.getJsonNumber("open").doubleValue());
            s.setHigh(o.getJsonNumber("high").doubleValue());
            s.setLow(o.getJsonNumber("low").doubleValue());
            s.setClose(o.getJsonNumber("close").doubleValue());
            s.setVolume(o.getJsonNumber("volume").longValue());
            s.setChange(o.getJsonNumber("change").doubleValue());
            s.setPercentChange(o.getJsonNumber("percent_change").doubleValue());
            s.setDatetime(o.getString("datetime"));

        }
        return s;
    }

    public static JsonObject toJSON(String json){
        JsonReader r = Json.createReader(new StringReader(json));
        return r.readObject();
    }

    

    
}
