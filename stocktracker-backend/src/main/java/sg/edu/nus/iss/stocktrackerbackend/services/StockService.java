package sg.edu.nus.iss.stocktrackerbackend.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


import sg.edu.nus.iss.stocktrackerbackend.models.Market;
import sg.edu.nus.iss.stocktrackerbackend.models.Portfolio;
import sg.edu.nus.iss.stocktrackerbackend.models.Stock;
import sg.edu.nus.iss.stocktrackerbackend.models.StockInfo;
import sg.edu.nus.iss.stocktrackerbackend.models.StockProfile;
import sg.edu.nus.iss.stocktrackerbackend.models.Trade;
import sg.edu.nus.iss.stocktrackerbackend.repositories.AccountRepository;
import sg.edu.nus.iss.stocktrackerbackend.repositories.StockRepository;

@Service
public class StockService {

    @Value("${twelve.data.url}")
    private String twelveDataUrl;

    @Value("${twelve.data.key}")
    private String twelveDataApiKey;

    @Value("${twelve.data.host}")
    private String twelveDataApiHost;

    @Autowired
    private StockRepository stockRepo;

    @Autowired
    private AccountRepository accountRepo;
    
    //function to get info from an external server using API.
   public Optional<Stock> getStockData(String symbol, String interval)
   throws IOException{
       System.out.println("twelveDataUrl: " + twelveDataUrl);
       System.out.println("twelveDataApiKey: " + twelveDataApiKey);
       System.out.println("twelveDataApiHost: " + twelveDataApiHost);
   
       String stockUrl = UriComponentsBuilder
                           .fromUriString(twelveDataUrl+"/quote")
                           .queryParam("symbol", symbol)
                           .queryParam("interval", interval)
                           .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Key", twelveDataApiKey);
        headers.set("X-RapidAPI-Host", twelveDataApiHost);


        RequestEntity req = RequestEntity.get(stockUrl)
                    .headers(headers)
                    .build();

       RestTemplate template= new RestTemplate();
        ResponseEntity<String> r  = template.exchange(req, 
        String.class);


       //r.getBody is a string response from api.
        System.out.println(">>>managed to exchange: >>>>" + r.getBody());
       Stock s = Stock.createUserObject(r.getBody());

       //for debugging
       String apiSymbol = s.getSymbol();
       String name = s.getName();
       System.out.println(">>>apiSymbol: " + apiSymbol);
       System.out.println(">>>name: " + name);

       return Optional.of(s);

   }

   public Optional<Stock> getLivePrice(String symbol, String interval)
   throws IOException{
       System.out.println("twelveDataUrl: " + twelveDataUrl);
       System.out.println("twelveDataApiKey: " + twelveDataApiKey);
       System.out.println("twelveDataApiHost: " + twelveDataApiHost);
   
       String stockUrl = UriComponentsBuilder
                           .fromUriString(twelveDataUrl+"/price")
                           .queryParam("symbol", symbol)
                           .queryParam("interval", interval)
                           .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Key", twelveDataApiKey);
        headers.set("X-RapidAPI-Host", twelveDataApiHost);


        RequestEntity req = RequestEntity.get(stockUrl)
                    .headers(headers)
                    .build();

       RestTemplate template= new RestTemplate();
        ResponseEntity<String> r  = template.exchange(req, 
        String.class);


       //r.getBody is a string response from api.
        System.out.println(">>>managed to exchange: >>>>" + r.getBody());
       Stock s = Stock.createPriceObject(r.getBody());

       //for debugging
       Double livePrice = s.getLivePrice();
    
       System.out.println(">>>LivePrice: " + livePrice);

       return Optional.of(s);

   }


    //function to get info from an external server using API.
   public Optional<StockProfile> getStockProfile(String symbol)
   throws IOException{
       System.out.println("twelveDataUrl: " + twelveDataUrl);
       System.out.println("twelveDataApiKey: " + twelveDataApiKey);
       System.out.println("twelveDataApiHost: " + twelveDataApiHost);
   
       String stockUrl = UriComponentsBuilder
                           .fromUriString(twelveDataUrl+"/profile")
                           .queryParam("symbol", symbol)
                           .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Key", twelveDataApiKey);
        headers.set("X-RapidAPI-Host", twelveDataApiHost);


        RequestEntity req = RequestEntity.get(stockUrl)
                    .headers(headers)
                    .build();

       RestTemplate template= new RestTemplate();
        ResponseEntity<String> r  = template.exchange(req, 
        String.class);


       //r.getBody is a string response from api.
        System.out.println(">>>managed to exchange: >>>>" + r.getBody());
       StockProfile sp = StockProfile.createUserObject(r.getBody());

       //for debugging
       String apiSymbol = sp.getSymbol();
       String name = sp.getName();
       System.out.println(">>>apiSymbol: " + apiSymbol);
       System.out.println(">>>name: " + name);

       return Optional.of(sp);

   }


      public Optional<StockProfile> getStockLogo(String symbol) throws IOException{
       System.out.println("twelveDataUrl: " + twelveDataUrl);
       System.out.println("twelveDataApiKey: " + twelveDataApiKey);
       System.out.println("twelveDataApiHost: " + twelveDataApiHost);
   
       String stockUrl = UriComponentsBuilder
                           .fromUriString(twelveDataUrl+"/logo")
                           .queryParam("symbol", symbol)
                           .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Key", twelveDataApiKey);
        headers.set("X-RapidAPI-Host", twelveDataApiHost);


        RequestEntity req = RequestEntity.get(stockUrl)
                    .headers(headers)
                    .build();

       RestTemplate template= new RestTemplate();
        ResponseEntity<String> r  = template.exchange(req, 
        String.class);


       //r.getBody is a string response from api.
        System.out.println(">>>managed to exchange logo: >>>>" + r.getBody());
       StockProfile spl = StockProfile.createLogo(r.getBody());

       //for debugging
       String stockLogo = spl.getLogoUrl();
       System.out.println(">>>stockLogo: " + stockLogo);

       return Optional.of(spl);

   }


    public Optional<Market> getMarketData(String symbol, String interval)
    throws IOException{
       System.out.println("twelveDataUrl: " + twelveDataUrl);
       System.out.println("twelveDataApiKey: " + twelveDataApiKey);
       System.out.println("twelveDataApiHost: " + twelveDataApiHost);
   
       String marketUrl = UriComponentsBuilder
                           .fromUriString(twelveDataUrl+"/quote")
                           .queryParam("symbol", symbol)
                           .queryParam("interval", interval)
                           .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Key", twelveDataApiKey);
        headers.set("X-RapidAPI-Host", twelveDataApiHost);


        RequestEntity req = RequestEntity.get(marketUrl)
                    .headers(headers)
                    .build();

       RestTemplate template= new RestTemplate();
        ResponseEntity<String> r  = template.exchange(req, 
        String.class);

       //r.getBody is a string response from api.
        System.out.println(">>>managed to exchange market data: >>>>" + r.getBody());
       Market m = Market.createUserObject(r.getBody());

       //for debugging
       String apiSymbol = m.getSymbol();
       String name = m.getName();
       System.out.println(">>>apiSymbol: " + apiSymbol);
       System.out.println(">>>name: " + name);

       return Optional.of(m);

   }

    public void saveMarketData(final Market market){
        stockRepo.saveMarketData(market);
    }

    public void saveStockData(Stock stock, String interval){
        stockRepo.saveStockData(stock, interval);
    }

    public void saveStockProfile(StockProfile stockProfile){
        stockRepo.saveStockProfile(stockProfile);
    }

    public Optional<Market> getMarketFromRedis(String symbol) throws IOException{
        System.out.println(">>>>>>>> I am in Redis service>>>>>>");
        return stockRepo.getMarketFromRedis(symbol);
    }

    public Optional<Stock> getStockFromRedis(String symbol, String interval) throws IOException{
        System.out.println(">>>>>>>> I am in Redis service for stocks>>>>>>");
        return stockRepo.getStockFromRedis(symbol, interval);
    }

    public Optional<StockProfile> getStockProfileFromRedis(String symbol) throws IOException{
        System.out.println(">>>>>>>> I am in Redis service for stocksProfile>>>>>>");
        return stockRepo.getStockProfileFromRedis(symbol);
    }
    
    
    public List<StockInfo> getStocksList(String exchange, String filter, int limit, int skip) {
    System.out.println(">>>>>>>> I am in Service >>> getStocksList");
    return stockRepo.getStocksList(exchange, filter, limit, skip);
    }
    
    public List<String> saveWatchlist(String[] symbols, String username){
        System.out.println(">>>>>>>> I am in Service >>> saveWatchList");
        stockRepo.saveWatchlist(username, symbols);
        // return stockRepo.getUserWatchlist(username);
        List<String> symbolList = Arrays.asList(symbols);
        return symbolList;
    }

    public List<String> getUserWatchlist(String username) {
        System.out.println(">>>>>>>> I am in Service >>> getUserWatchlist");
        return stockRepo.getUserWatchlist(username);
    }


    public Portfolio getPortfolioData(String accountId, String symbol, String interval) throws IOException {
        System.out.println(">>>>>>>> I am in getPortfolioDataService>>>>>>");
        Optional<Stock> s;
        // Portfolio p = new Portfolio();
        Optional<Trade> t = accountRepo.getTradeData(accountId, symbol);
        Trade trade = t.get();

        Stock stock;
            s = getLivePrice(symbol, interval);
            stock = s.get();

        Portfolio calculatedP = getCalculations(trade.getUnits(), stock.getLivePrice(),trade.getTotal());
        System.out.println(">>>>The total percentage change is >>>>>" + calculatedP.getTotalPercentageChange() );
        Portfolio p = new Portfolio(trade.getAccountId(), trade.getSymbol(), trade.getStockName(),
                        trade.getExchange(), trade.getCurrency(), trade.getUnits(), trade.getPrice(),trade.getTotal(),
                        stock.getLivePrice(), calculatedP.getTotalCurrentPrice(), 
                        calculatedP.getTotalReturn(), calculatedP.getTotalPercentageChange(), LocalDate.now()  );
            System.out.println(">>> The stock price is>>>" + stock.getLivePrice());

        return p;
        
    }

    public List<Portfolio> getAnnualisedPortfolioData(String accountId, String interval) throws IOException {
    System.out.println(">>>>>>>> I am in getPortfolioDataService>>>>>>");
    Optional<Stock> s;
    // Portfolio p = new Portfolio();
    List<Trade> trades = accountRepo.getAnnualisedTradeData(accountId);
    
    List<Portfolio> portfolio = new ArrayList<Portfolio>();
    if (!trades.isEmpty()) {
    for (Trade trade : trades) {
        Stock stock;
        System.out.println(">>> The totalPurchase for trade symbol" + trade.getSymbol()+ "is>>>" + trade.getTotal());
        s = getLivePrice(trade.getSymbol(), interval);
        stock = s.get();
        Portfolio calculatedP = getAnnualisedCalculations(trade.getUnits(), stock.getLivePrice(),trade.getTotal(), trade.getDate());
        System.out.println(">>> The annualisedProfit is>>>" + calculatedP.getAnnualisedProfit());
        Portfolio p = new Portfolio(trade.getSymbol(), trade.getStockName(),
        trade.getExchange(), trade.getCurrency(), trade.getUnits(), trade.getPrice(),trade.getTotal(),
        stock.getLivePrice(), trade.getUnits()*stock.getLivePrice(), 
        calculatedP.getTotalReturn(), calculatedP.getAnnualisedProfit(), trade.getDate());
        System.out.println(">>> The stock price is>>>" + stock.getLivePrice());
        portfolio.add(p);
    }
    } else {
        // Handle the case when no trades are found
        System.out.println("No trades found.");
    }

    return portfolio;
    
}


    private Portfolio getCalculations(Double units, Double currentUnitPrice, Double totalBuyPrice){

        Double totalCurrentPrice = units*currentUnitPrice; 
        Double totalReturn = totalCurrentPrice - totalBuyPrice;
        Double totalPercentageChange = (totalReturn/totalBuyPrice)*100;

        Portfolio p = new Portfolio(totalCurrentPrice, totalReturn, totalPercentageChange);
        return p;
    }


    private Portfolio getAnnualisedCalculations(Double units, Double currentUnitPrice, Double totalBuyPrice, LocalDate buyDate) {
        Double totalCurrentPrice = units * currentUnitPrice;
        Double totalReturn = totalCurrentPrice - totalBuyPrice;

        System.out.println("The total current price is: " + totalCurrentPrice);
        System.out.println("The total return is: " + totalReturn);

        LocalDate currentDate = LocalDate.now();
        // Period period = Period.between(buyDate, currentDate);
        long daysHeld = ChronoUnit.DAYS.between(buyDate, currentDate);
        if (daysHeld == 0){
            daysHeld = 1;
        }

        System.out.println("The number of days held is: " + daysHeld);

        Double totalPercentageChange = (totalReturn / totalBuyPrice) * 100;
        BigDecimal percentageChange = BigDecimal.valueOf(totalPercentageChange/100);
        BigDecimal exponent = BigDecimal.valueOf(365.0 / daysHeld);
        BigDecimal one = BigDecimal.ONE;
        double base = one.add(percentageChange).doubleValue();
        double exp = exponent.doubleValue();
        double annualisedProfitValue = (Math.pow(base, exp) - 1)*100;

        BigDecimal annualisedProfit = BigDecimal.valueOf(annualisedProfitValue);

        System.out.println("Percentage Change: " + percentageChange.toString());
        System.out.println("Exponent: " + exponent.toString());
        System.out.println("Annualised Profit: " + annualisedProfit.toString());

        Portfolio p = new Portfolio(totalReturn, annualisedProfit);
        return p;
    }

}
