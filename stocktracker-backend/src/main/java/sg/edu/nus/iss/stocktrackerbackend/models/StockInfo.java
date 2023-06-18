package sg.edu.nus.iss.stocktrackerbackend.models;

public record StockInfo(String name, String symbol, String currency,
                        String exchange, String country, String type){}
