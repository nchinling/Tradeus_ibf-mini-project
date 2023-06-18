package sg.edu.nus.iss.stocktrackerbackend.services;

import java.io.IOException;
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
import sg.edu.nus.iss.stocktrackerbackend.models.Stock;
import sg.edu.nus.iss.stocktrackerbackend.models.StockInfo;
import sg.edu.nus.iss.stocktrackerbackend.models.StockProfile;
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

}
