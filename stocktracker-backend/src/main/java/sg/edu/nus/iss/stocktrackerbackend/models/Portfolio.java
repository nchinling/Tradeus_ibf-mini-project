package sg.edu.nus.iss.stocktrackerbackend.models;

import java.time.LocalDate;

public class Portfolio {

    private String accountId;
    private String symbol;
    private String stockName;
    private String exchange;
    private String currency;
    private Double units;
    private Double buyUnitPrice;
    private Double buyTotalPrice;
    private Double unitCurrentPrice;
    private Double totalCurrentPrice;
    private Double totalReturn;
    private Double totalPercentageChange;
    private Double annualisedProfit;


    private LocalDate dateTime;
    public Portfolio() {
    }

    public Portfolio(Double totalCurrentPrice, Double totalReturn, Double totalPercentageChange) {
        this.totalCurrentPrice = totalCurrentPrice;
        this.totalReturn = totalReturn;
        this.totalPercentageChange = totalPercentageChange;
    }


    public Portfolio(String accountId, String symbol, String stockName, String exchange, String currency, Double units,
            Double buyUnitPrice, Double buyTotalPrice, Double unitCurrentPrice, Double totalCurrentPrice,
            Double totalReturn, Double totalPercentageChange, LocalDate dateTime) {
        this.accountId = accountId;
        this.symbol = symbol;
        this.stockName = stockName;
        this.exchange = exchange;
        this.currency = currency;
        this.units = units;
        this.buyUnitPrice = buyUnitPrice;
        this.buyTotalPrice = buyTotalPrice;
        this.unitCurrentPrice = unitCurrentPrice;
        this.totalCurrentPrice = totalCurrentPrice;
        this.totalReturn = totalReturn;
        this.totalPercentageChange = totalPercentageChange;
        this.dateTime = dateTime;
    }

    public Portfolio(String accountId, String symbol, String stockName, String exchange, String currency, Double units,
            Double buyUnitPrice, Double buyTotalPrice, Double unitCurrentPrice, Double totalCurrentPrice,
            Double totalReturn, Double totalPercentageChange, Double annualisedProfit, LocalDate dateTime) {
        this.accountId = accountId;
        this.symbol = symbol;
        this.stockName = stockName;
        this.exchange = exchange;
        this.currency = currency;
        this.units = units;
        this.buyUnitPrice = buyUnitPrice;
        this.buyTotalPrice = buyTotalPrice;
        this.unitCurrentPrice = unitCurrentPrice;
        this.totalCurrentPrice = totalCurrentPrice;
        this.totalReturn = totalReturn;
        this.totalPercentageChange = totalPercentageChange;
        this.annualisedProfit = annualisedProfit;
        this.dateTime = dateTime;
    }



    public String getAccountId() {
        return accountId;
    }
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    public String getSymbol() {
        return symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public String getStockName() {
        return stockName;
    }
    public void setStockName(String stockName) {
        this.stockName = stockName;
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
    public Double getUnits() {
        return units;
    }
    public void setUnits(Double units) {
        this.units = units;
    }
    public Double getBuyUnitPrice() {
        return buyUnitPrice;
    }
    public void setBuyUnitPrice(Double buyUnitPrice) {
        this.buyUnitPrice = buyUnitPrice;
    }
    public Double getBuyTotalPrice() {
        return buyTotalPrice;
    }
    public void setBuyTotalPrice(Double buyTotalPrice) {
        this.buyTotalPrice = buyTotalPrice;
    }
    public Double getUnitCurrentPrice() {
        return unitCurrentPrice;
    }
    public void setUnitCurrentPrice(Double unitCurrentPrice) {
        this.unitCurrentPrice = unitCurrentPrice;
    }
    public Double getTotalCurrentPrice() {
        return totalCurrentPrice;
    }
    public void setTotalCurrentPrice(Double totalCurrentPrice) {
        this.totalCurrentPrice = totalCurrentPrice;
    }
    public Double getTotalReturn() {
        return totalReturn;
    }
    public void setTotalReturn(Double totalReturn) {
        this.totalReturn = totalReturn;
    }
    public Double getTotalPercentageChange() {
        return totalPercentageChange;
    }
    public void setTotalPercentageChange(Double totalPercentageChange) {
        this.totalPercentageChange = totalPercentageChange;
    }

    public Double getAnnualisedProfit() {
        return annualisedProfit;
    }

    public void setAnnualisedProfit(Double annualisedProfit) {
        this.annualisedProfit = annualisedProfit;
    }

    public LocalDate getDateTime() {
        return dateTime;
    }
    public void setDateTime(LocalDate dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Portfolio [accountId=" + accountId + ", symbol=" + symbol + ", stockName=" + stockName + ", exchange="
                + exchange + ", currency=" + currency + ", units=" + units + ", buyUnitPrice=" + buyUnitPrice
                + ", buyTotalPrice=" + buyTotalPrice + ", unitCurrentPrice=" + unitCurrentPrice + ", totalCurrentPrice="
                + totalCurrentPrice + ", totalReturn=" + totalReturn + ", totalPercentageChange="
                + totalPercentageChange + ", annualisedProfit=" + annualisedProfit + ", dateTime=" + dateTime + "]";
    }

    




    

}
