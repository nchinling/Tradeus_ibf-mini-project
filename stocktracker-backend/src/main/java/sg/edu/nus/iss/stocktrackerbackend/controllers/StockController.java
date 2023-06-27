package sg.edu.nus.iss.stocktrackerbackend.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import sg.edu.nus.iss.stocktrackerbackend.models.Market;
import sg.edu.nus.iss.stocktrackerbackend.models.Portfolio;
import sg.edu.nus.iss.stocktrackerbackend.models.Stock;
import sg.edu.nus.iss.stocktrackerbackend.models.StockInfo;
import sg.edu.nus.iss.stocktrackerbackend.models.StockProfile;
import sg.edu.nus.iss.stocktrackerbackend.models.Watchlist;
import sg.edu.nus.iss.stocktrackerbackend.services.StockService;


@Controller
@RequestMapping(path="/api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins="*")
// @CrossOrigin(origins = "http://localhost:4200")
public class StockController {
    
    @Autowired
    private StockService stockSvc;




    @GetMapping(path="/quote/stock")
    @ResponseBody
    public ResponseEntity<String> getStockData(@RequestParam(required=true) String symbol,
    @RequestParam(defaultValue = "1day",required=false) String interval,
    @RequestParam(required=false) String username) throws IOException{
        // Integer num = weatherSvc.getWeather(city);
        System.out.println("I am in getStockData server");
        System.out.println(">>>>>>>>Symbol in controller>>>>>" + symbol);
        System.out.println(">>>>>>>>Interval in controller>>>>>" + interval);
        
        //Obtain from redis cache
        Optional<Stock> stk = stockSvc.getStockFromRedis(symbol, interval);
        if (stk.isPresent()){
            Stock stock = stk.get();
          
            System.out.println("Obtained stock data from Redis");
     
            JsonObject resp = Json.createObjectBuilder()
                .add("symbol", stock.getSymbol())
                .add("name", stock.getName())
                .add("exchange",stock.getExchange() )
                .add("currency", stock.getCurrency())
                .add("open", stock.getOpen())
                .add("high", stock.getHigh())
                .add("low", stock.getLow())
                .add("close", stock.getClose())
                .add("previous_close", stock.getPreviousClose())
                .add("volume", stock.getVolume())
                .add("change", stock.getChange())
                .add("percent_change", stock.getPercentChange())
                .add("datetime", stock.getDatetime())
                .build();
                System.out.println(">>>resp: " + resp);
            
            return ResponseEntity.ok(resp.toString());
        }
        
        //Obtain from api
        Optional<Stock> s = stockSvc.getStockData(symbol, interval);
        if (s.isPresent()) {
            Stock stock = s.get();

            //save stock data in redis/mongo for quick retrieval
            stockSvc.saveStockData(stock, interval);

            System.out.println("Obtained stock data from API");

            JsonObject resp = Json.createObjectBuilder()
                .add("symbol", stock.getSymbol())
                .add("name", stock.getName())
                .add("exchange",stock.getExchange() )
                .add("currency", stock.getCurrency())
                .add("open", stock.getOpen())
                .add("high", stock.getHigh())
                .add("low", stock.getLow())
                .add("close", stock.getClose())
                .add("previous_close", stock.getPreviousClose())
                .add("volume", stock.getVolume())
                .add("change", stock.getChange())
                .add("percent_change", stock.getPercentChange())
                .add("datetime", stock.getDatetime())
                .build();
                System.out.println(">>>resp: " + resp);
            
            return ResponseEntity.ok(resp.toString());
        } 
        // Handle the case when the Optional is empty
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body("Stock information not available for the provided symbol.");
        
        
    }

    
    @GetMapping(path="/stock/profile/{symbol}")
    @ResponseBody
    public ResponseEntity<String> getStockProfile(@PathVariable String symbol) throws IOException{

        System.out.println(">>>>>>>>Symbol in StockProfile>>>>>" + symbol);
    
        //Obtain from redis cache
        Optional<StockProfile> osp = stockSvc.getStockProfileFromRedis(symbol);
        if (osp.isPresent()){
            System.out.println(">>>>>>>>Retrieved StockProfile from redi>>>>>" + symbol);
            StockProfile stockProfile = osp.get();
          
            System.out.println("Obtained stock data from Redis");
     
            JsonObject resp = Json.createObjectBuilder()
                .add("symbol", stockProfile.getSymbol())
                .add("name", stockProfile.getName())
                .add("sector", stockProfile.getSector())
                .add("industry", stockProfile.getIndustry())
                .add("ceo", stockProfile.getCeo())
                .add("employees", stockProfile.getEmployees())
                .add("website", stockProfile.getWebsite())
                .add("description", stockProfile.getDescription())
                .add("logoUrl", stockProfile.getLogoUrl())
                .build();
                System.out.println(">>>built the stock profile>>>> " + resp);
            
            return ResponseEntity.ok(resp.toString());
        }
        
        //Obtain from api
        Optional<StockProfile> sp = stockSvc.getStockProfile(symbol);
        Optional<StockProfile> spl = stockSvc.getStockLogo(symbol);
        if (sp.isPresent()) {
            StockProfile stockProfile = sp.get();
        
            StockProfile stockLogo = spl.get();
            stockProfile.setLogoUrl(stockLogo.getLogoUrl());

            //save stock data in redis/mongo for quick retrieval
            stockSvc.saveStockProfile(stockProfile);

            System.out.println("Obtained stock data from API");

            JsonObject resp = Json.createObjectBuilder()
                .add("symbol", stockProfile.getSymbol())
                .add("name", stockProfile.getName())
                .add("sector", stockProfile.getSector())
                .add("industry", stockProfile.getIndustry())
                .add("ceo", stockProfile.getCeo())
                .add("employees", stockProfile.getEmployees())
                .add("website", stockProfile.getWebsite())
                .add("description", stockProfile.getDescription())
                .add("logoUrl", stockProfile.getLogoUrl())
                .build();
                System.out.println(">>>built the stock profile>>>> " + resp);
            
            return ResponseEntity.ok(resp.toString());
        } 
        // Handle the case when the Optional is empty
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body("Stock Profile not available for the provided symbol.");
        
    }



    @GetMapping(path="/quote/market", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getMarketData(@RequestParam(required=true) String symbol,
    @RequestParam(defaultValue = "1day",required=false) String interval) throws IOException{
        // Integer num = weatherSvc.getWeather(city);
        System.out.println("I am in getMarketData server");
        System.out.println(">>>>>>>>Symbol in controller>>>>>" + symbol);
        System.out.println(">>>>>>>>Interval in controller>>>>>" + interval);

        Optional<Market> mkt = stockSvc.getMarketFromRedis(symbol);
        if (mkt.isPresent()){
            Market market = mkt.get();
          
            System.out.println("Obtained market data from Redis");
     
            JsonObject resp = Json.createObjectBuilder()
                .add("symbol", market.getSymbol())
                .add("name", market.getName())
                .add("datetime",market.getDateTime())
                .add("close",market.getClose())
                .add("change", market.getChange())
                .add("percentage_change", market.getPercentageChange())
                .build();
                System.out.println(">>>FromRedisresp: " + resp);
            
            return ResponseEntity.ok(resp.toString());
        }
        

        Optional<Market> m = stockSvc.getMarketData(symbol, interval);
        if (m.isPresent()) {
            Market market = m.get();

            //to save market data in redis/mongo for quick retrieval
            stockSvc.saveMarketData(market);

            System.out.println("Obtained stock data from API");


            JsonObject resp = Json.createObjectBuilder()
                .add("symbol", market.getSymbol())
                .add("name", market.getName())
                .add("datetime",market.getDateTime())
                .add("close",market.getClose())
                .add("change", market.getChange())
                .add("percentage_change", market.getPercentageChange())
                .build();
                System.out.println(">>>resp: " + resp);
            
            return ResponseEntity.ok(resp.toString());
        } 
        // Handle the case when the Optional is empty
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body("Market information not available");
        
    }


    @GetMapping(path="quote/watchlist", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getWatchlistData(@RequestParam(required=true) String symbol,
    @RequestParam(defaultValue = "1day",required=false) String interval) throws IOException{
        // Integer num = weatherSvc.getWeather(city);
        System.out.println("I am in getStockData server");
        System.out.println(">>>>>>>>Symbol in controller>>>>>" + symbol);
        System.out.println(">>>>>>>>Interval in controller>>>>>" + interval);
          
        //Obtain from redis cache
        Optional<Stock> stk = stockSvc.getStockFromRedis(symbol, interval);
        if (stk.isPresent()){
            Stock stock = stk.get();
          
            System.out.println("Obtained stock data from Redis");
     
            JsonObject resp = Json.createObjectBuilder()
                .add("symbol", stock.getSymbol())
                .add("name", stock.getName())
                .add("exchange",stock.getExchange() )
                .add("currency", stock.getCurrency())
                .add("open", stock.getOpen())
                .add("high", stock.getHigh())
                .add("low", stock.getLow())
                .add("close", stock.getClose())
                .add("previous_close", stock.getPreviousClose())
                .add("volume", stock.getVolume())
                .add("change", stock.getChange())
                .add("percent_change", stock.getPercentChange())
                .add("datetime", stock.getDatetime())
                .build();
                System.out.println(">>>resp: " + resp);
            
            return ResponseEntity.ok(resp.toString());
        }
        
        //Obtain from api
        Optional<Stock> s = stockSvc.getStockData(symbol, interval);
        if (s.isPresent()) {
            Stock stock = s.get();

            //save stock data in redis/mongo for quick retrieval
            stockSvc.saveStockData(stock, interval);

            System.out.println("Obtained stock data from API");

            JsonObject resp = Json.createObjectBuilder()
                .add("symbol", stock.getSymbol())
                .add("name", stock.getName())
                .add("exchange",stock.getExchange() )
                .add("currency", stock.getCurrency())
                .add("open", stock.getOpen())
                .add("high", stock.getHigh())
                .add("low", stock.getLow())
                .add("close", stock.getClose())
                .add("previous_close", stock.getPreviousClose())
                .add("volume", stock.getVolume())
                .add("change", stock.getChange())
                .add("percent_change", stock.getPercentChange())
                .add("datetime", stock.getDatetime())
                .build();
                System.out.println(">>>resp: " + resp);
            
            return ResponseEntity.ok(resp.toString());
        } 
        // Handle the case when the Optional is empty
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body("Stock information not available for the provided symbol.");
        
    }


    @GetMapping(path="/watchlist", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getWatchlist(@RequestParam(required=true) String username) throws IOException{
        // Integer num = weatherSvc.getWeather(city);
        System.out.println("I am in getWatchlist server");
        System.out.println(">>>>>>>>username in controller>>>>>" + username);
       
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
		List<String> userWatchlist = stockSvc.getUserWatchlist(username);
		userWatchlist.stream()
			.map(each -> Json.createObjectBuilder()
						.add("symbol", each)

						.build()
			)
			.forEach(json -> arrBuilder.add(json));

		return ResponseEntity.ok(arrBuilder.build().toString());
       
    }


    @GetMapping(path="/stocklist", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
	public ResponseEntity<String> getStocks(
            @RequestParam(defaultValue = "") String exchange,
			@RequestParam(defaultValue = "") String filter,
			@RequestParam(defaultValue = "10") int limit,
			@RequestParam(defaultValue = "0") int skip) {
    

		JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        System.out.println(">>>The exchange in controller is >>> " + exchange);
		List<StockInfo> stockInfoList = stockSvc.getStocksList(exchange,filter, skip, limit);
		stockInfoList.stream()
			.map(stockinfo -> Json.createObjectBuilder()
						.add("name", stockinfo.name())
						.add("symbol", stockinfo.symbol())
                        .add("currency", stockinfo.currency())
						.add("exchange", stockinfo.exchange())
                        .add("country", stockinfo.country())
						.add("type", stockinfo.type())
						.build()
			)
			.forEach(json -> arrBuilder.add(json));

		return ResponseEntity.ok(arrBuilder.build().toString());
	}


    @PostMapping(path="/watchlist", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    // public ResponseEntity<String> saveWatchlist(@RequestBody String[] symbols) {
        public ResponseEntity<String> saveWatchlist(@RequestBody Watchlist request) {
        String[] symbols = request.getSymbols();
        for (String symbol: symbols){
            System.out.println(">>>>The symbol received is>>>" + symbol);
        }

        String username = request.getUsername();
        // String [] symbols = watchlist.getSymbols();
        System.out.println(">>>> I am in stock server watchlist >>>>>>>>>>>>");
        //   System.out.println(payload);
        // JsonReader reader = Json.createReader(new StringReader(payload));
        // JsonObject req = reader.readObject();

        System.out.println(">>>>>> username is >>>>> " +username);
        

        JsonObject resp = null;

            List<String> symbolsList = stockSvc.saveWatchlist(symbols, username);
            for (String symbol : symbolsList) {
                System.out.println(">>>>>>>>>Sending back to client after deletion<<<<<<<<<" +symbol);
            }

            JsonArrayBuilder arrBuilder = Json.createArrayBuilder(symbolsList);
    
            resp = Json.createObjectBuilder()
                .add("symbols", arrBuilder.build())
                .build();
    
            return ResponseEntity.ok(resp.toString());


	}

 
    @GetMapping(path="/quote/portfolio")
    @ResponseBody
    public ResponseEntity<String> getPortfolioData(@RequestParam(required=true) String symbol,
    @RequestParam(defaultValue = "1day",required=false) String interval,
    @RequestParam(required=false) String account_id) throws IOException{
        // Integer num = weatherSvc.getWeather(city);
        System.out.println("I am in getStockData server");
        System.out.println(">>>>>>>>Symbol in controller>>>>>" + symbol);
        System.out.println(">>>>>>>>Interval in controller>>>>>" + interval);
        System.out.println(">>>>>>>>accountId in controller>>>>>" + account_id);

        String accountId = account_id;
        

        //Obtain data from api and service 
        // Optional<Portfolio> p = stockSvc.getPortfolioData(accountId, symbol, interval);
        // if (p.isPresent()) {
        //     Portfolio portfolio = p.get();
            Portfolio portfolio = stockSvc.getPortfolioData(accountId, symbol, interval);

            //save portfolio data in redis/mongo for quick retrieval
            // stockSvc.saveStockData(stock, interval);

            System.out.println("Obtained portfolio data from API");
            System.out.println(">>>>>Portfolio stockname is>>>>" + portfolio.getStockName());

            JsonObject resp = Json.createObjectBuilder()
                .add("account_id", portfolio.getAccountId())
                .add("symbol", portfolio.getSymbol())
                .add("stock_name", portfolio.getStockName())
                .add("exchange", portfolio.getExchange())
                .add("currency", portfolio.getCurrency())
                .add("units", portfolio.getUnits())
                .add("buy_unit_price", portfolio.getBuyUnitPrice())
                .add("buy_total_price", portfolio.getBuyTotalPrice())
                .add("unit_current_price", portfolio.getUnitCurrentPrice())
                .add("total_current_price", portfolio.getTotalCurrentPrice())
                .add("total_return", portfolio.getTotalReturn())
                .add("total_percentage_change", portfolio.getTotalPercentageChange())
                // .add("annualised_profit", portfolio.getAnnualisedProfit())
                .add("datetime", portfolio.getDateTime().toString())
                .build();
                System.out.println(">>>resp: " + resp);
                System.out.println(">>>sending back portfolio data.>>>>>Hooray: ");

            System.out.println(">>>>>Portfolio" + portfolio.getStockName() +"total units is>>>>" + portfolio.getUnits());
            return ResponseEntity.ok(resp.toString());
        // } 
        // // Handle the case when the Optional is empty
        // return ResponseEntity.status(HttpStatus.NOT_FOUND)
        // .body("Portfolio information not available for the provided symbol.");
        
        
    }


    
    @GetMapping(path="/quote/portfolio/annualised")
    @ResponseBody
    public ResponseEntity<String> getAnnualisedPortfolioData(@RequestParam(defaultValue = "1day",required=false) String interval,
    @RequestParam(required=true) String account_id) throws IOException{
        // Integer num = weatherSvc.getWeather(city);
        System.out.println("I am in getStockData server");

        System.out.println(">>>>>>>>Interval in controller>>>>>" + interval);
        System.out.println(">>>>>>>>accountId in controller>>>>>" + account_id);

        String accountId = account_id;
        

  
            List<Portfolio> portfolio = stockSvc.getAnnualisedPortfolioData(accountId, interval);

            //save portfolio data in redis/mongo for quick retrieval
            // stockSvc.saveStockData(stock, interval);

            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

            for (Portfolio portfolioItem : portfolio) {
                JsonObjectBuilder portfolioBuilder = Json.createObjectBuilder()
                        .add("symbol", portfolioItem.getSymbol())
                        .add("stock_name", portfolioItem.getStockName())
                        .add("exchange", portfolioItem.getExchange())
                        .add("currency", portfolioItem.getCurrency())
                        .add("units", portfolioItem.getUnits())
                        .add("buy_unit_price", portfolioItem.getBuyUnitPrice())
                        .add("buy_total_price", portfolioItem.getBuyTotalPrice())
                        .add("unit_current_price", portfolioItem.getUnitCurrentPrice())
                        .add("total_current_price", portfolioItem.getTotalCurrentPrice())
                        .add("total_return", portfolioItem.getTotalReturn())
                        .add("annualised_profit", portfolioItem.getAnnualisedProfit())
                        .add("datetime", portfolioItem.getDateTime().toString());
                        
                arrayBuilder.add(portfolioBuilder);
            }

            JsonArray respArray = arrayBuilder.build();
            System.out.println(">>>sending back jsonarray annualised portfolio data.>>>>>Hooray: " + respArray);
            return ResponseEntity.ok(respArray.toString());
        // } 
        // // Handle the case when the Optional is empty
        // return ResponseEntity.status(HttpStatus.NOT_FOUND)
        // .body("Portfolio information not available for the provided symbol.");
        
        
    }



}




