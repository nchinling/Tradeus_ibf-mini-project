package sg.edu.nus.iss.stocktrackerbackend.models;

public record StockInfo(String name, String symbol, String currency,
                        String exchange, String country, String type){}

// public class StockInfo {

//     private String name;
//     private String symbol;
//     private String currency;
//     private String exchange;
//     private String country;
//     private String type;
//     public StockInfo() {
//     }
//     public StockInfo(String name, String symbol, String currency, String exchange, String country, String type) {
//         this.name = name;
//         this.symbol = symbol;
//         this.currency = currency;
//         this.exchange = exchange;
//         this.country = country;
//         this.type = type;
//     }
//     public String getName() {
//         return name;
//     }
//     public void setName(String name) {
//         this.name = name;
//     }
//     public String getSymbol() {
//         return symbol;
//     }
//     public void setSymbol(String symbol) {
//         this.symbol = symbol;
//     }
//     public String getCurrency() {
//         return currency;
//     }
//     public void setCurrency(String currency) {
//         this.currency = currency;
//     }
//     public String getExchange() {
//         return exchange;
//     }
//     public void setExchange(String exchange) {
//         this.exchange = exchange;
//     }
//     public String getCountry() {
//         return country;
//     }
//     public void setCountry(String country) {
//         this.country = country;
//     }
//     public String getType() {
//         return type;
//     }
//     public void setType(String type) {
//         this.type = type;
//     }
//     @Override
//     public String toString() {
//         return "StockInfo [name=" + name + ", symbol=" + symbol + ", currency=" + currency + ", exchange=" + exchange
//                 + ", country=" + country + ", type=" + type + "]";
//     }


// }

