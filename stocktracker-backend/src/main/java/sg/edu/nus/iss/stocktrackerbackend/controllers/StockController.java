package sg.edu.nus.iss.stocktrackerbackend.controllers;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.stocktrackerbackend.models.Stock;
import sg.edu.nus.iss.stocktrackerbackend.services.StockService;

@Controller
@RequestMapping(path="/api/stock", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins="*")
// @CrossOrigin(origins = "http://localhost:4200")
public class StockController {
    
      @Autowired
    private StockService stockSvc;

    @GetMapping(path="/quote")
    @ResponseBody
    public ResponseEntity<String> getStockData(@RequestParam(required=true) String symbol,
    @RequestParam(defaultValue = "1day",required=false) String interval) throws IOException{
        // Integer num = weatherSvc.getWeather(city);
        System.out.println("I am in getStockData server");
        System.out.println(">>>>>>>>Symbol in controller>>>>>" + symbol);
        System.out.println(">>>>>>>>Interval in controller>>>>>" + interval);
        
        // Optional<Weather> wr = weatherSvc.getWeatherFromRedis(city);
        // if (wr.isPresent()){
        //     Weather weather = wr.get();
        //     System.out.println("Obtained weather data from Redis");
        //     //no need to save
        //     // weatherSvc.save(weather);

        //     String sunriseTime = DateTimeConverter(weather.getSunrise());
        //     String sunsetTime = DateTimeConverter(weather.getSunset());
        //     JsonObject resp = Json.createObjectBuilder()
        //         .add("city", weather.getCity())
        //         .add("temperature", weather.getTemperature())
        //         .add("visibility",weather.getVisibility() )
        //         .add("sunrise", sunriseTime)
        //         .add("sunset", sunsetTime)
        //         .add("description", weather.getWeathercondition().get(0).getDescription())
        //         .add("mainWeather", weather.getWeathercondition().get(0).getMain())
        //         .build();
        //         System.out.println(">>>FromRedisresp: " + resp);
            
        //     return ResponseEntity.ok(resp.toString());
        // }
        
        Optional<Stock> s = stockSvc.getStockData(symbol, interval);
        if (s.isPresent()) {
            Stock stock = s.get();
            //to save stock data in redis/mongo for quick retrieval
            // stockSvc.save(stock);

            System.out.println("Obtained stock data from API");
            // String sunriseTime = DateTimeConverter(weather.getSunrise());
            // String sunsetTime = DateTimeConverter(weather.getSunset());

            JsonObject resp = Json.createObjectBuilder()
                .add("symbol", stock.getSymbol())
                .add("name", stock.getName())
                .add("exchange",stock.getExchange() )
                .add("currency", stock.getCurrency())
                .build();
                System.out.println(">>>resp: " + resp);
            
            return ResponseEntity.ok(resp.toString());
        } 
        // Handle the case when the Optional is empty
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body("Stock information not available for the provided symbol.");
        
        
    }


}
