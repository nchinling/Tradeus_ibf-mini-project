package sg.edu.nus.iss.stocktrackerbackend.models;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.math.BigDecimal;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;

public class Market {
    private String symbol;
    private String name;
    private String dateTime;
    private double close;
    private double change;
    private double percentageChange;

    public Market() {
    }
    public Market(String symbol, String name, String dateTime, double close, double change, double percentageChange) {
        this.symbol = symbol;
        this.name = name;
        this.dateTime = dateTime;
        this.close = close;
        this.change = change;
        this.percentageChange = percentageChange;
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
    public String getDateTime() {
        return dateTime;
    }
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
    public double getClose() {
        return close;
    }
    public void setClose(double close) {
        this.close = close;
    }
    public double getChange() {
        return change;
    }
    public void setChange(double change) {
        this.change = change;
    }
    public double getPercentageChange() {
        return percentageChange;
    }
    public void setPercentageChange(double percentageChange) {
        this.percentageChange = percentageChange;
    }
    @Override
    public String toString() {
        return "Market [symbol=" + symbol + ", name=" + name + ", dateTime=" + dateTime + ", close=" + close
                + ", change=" + change + ", percentageChange=" + percentageChange + "]";
    }


    public static Market createUserObject(String json) throws IOException {
        Market m = new Market();
        try(InputStream is = new ByteArrayInputStream(json.getBytes())){
            JsonReader r = Json.createReader(is);
            JsonObject o = r.readObject();
            m.setSymbol(o.getString("symbol"));
            m.setName(o.getString("name"));
            m.setDateTime(o.getString("datetime"));
            m.setClose(Double.parseDouble(o.getString("close")));
            m.setChange(Double.parseDouble(o.getString("change")));
            m.setPercentageChange(Double.parseDouble(o.getString("percent_change")));
           
            
        }
        return m;
    }

    
    public JsonObject toJSON(){
        
        return Json.createObjectBuilder()
                .add("symbol", this.getSymbol())
                .add("name", this.getName())
                .add("dateTime", this.getDateTime())
                .add("close", this.getClose())
                .add("change", this.getChange())
                .add("percentageChange", this.getPercentageChange())
                .build();
    }

    
    public static Market createUserObjectFromRedis(String jsonStr) throws IOException{
        Market m = new Market();
        try(InputStream is = new ByteArrayInputStream(jsonStr.getBytes())) {
            JsonObject o = toJSON(jsonStr);
            m.setSymbol(o.getString("symbol"));
            m.setName(o.getString("name"));
            m.setDateTime(o.getString("dateTime"));
            m.setClose(o.getJsonNumber("close").doubleValue());
            m.setChange(o.getJsonNumber("change").doubleValue());
            m.setPercentageChange(o.getJsonNumber("percentageChange").doubleValue());
    
        }
        return m;
    }

    public static JsonObject toJSON(String json){
        JsonReader r = Json.createReader(new StringReader(json));
        return r.readObject();
    }

}



