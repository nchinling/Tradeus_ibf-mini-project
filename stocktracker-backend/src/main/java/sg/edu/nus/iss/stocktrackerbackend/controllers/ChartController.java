package sg.edu.nus.iss.stocktrackerbackend.controllers;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.stocktrackerbackend.models.Chart;
import sg.edu.nus.iss.stocktrackerbackend.services.ChartService;

@Controller
@RequestMapping(path="/api", produces = MediaType.APPLICATION_JSON_VALUE)
// @CrossOrigin(origins="*")
public class ChartController {
    
    @Autowired
    private ChartService chartSvc;

    @GetMapping(path="/timeseries")
    @ResponseBody
    public ResponseEntity<String> getTimeSeries(@RequestParam(required=true) String symbol,
    @RequestParam(defaultValue = "1min") String interval,
    @RequestParam(required=true) String dataPoints) throws IOException{
        // Integer num = weatherSvc.getWeather(city);
        System.out.println("I am in getChartData server");
        System.out.println(">>>>>>>>Symbol in controller>>>>>" + symbol);
        System.out.println(">>>>>>>>Interval in controller>>>>>" + interval);
        System.out.println(">>>>>>>>Data points in controller>>>>>" + dataPoints);
        
        
        //Obtain from api
        Optional<Chart> c = chartSvc.getTimeSeries(symbol, interval, dataPoints);
        if (c.isPresent()) {
            Chart chart = c.get();

            //save stock data in redis/mongo for quick retrieval
            // chartSvc.saveChartData(symbol, interval, dataPoints);

            System.out.println("Obtained stock data from API");

            JsonObject resp = Json.createObjectBuilder()
                .add("symbol", chart.getSymbol())
                .add("interval", chart.getInterval())
                .add("datetime", createJsonArray(chart.getDateTime()))
                .add("high", createJsonArray(chart.getHigh()))
                .add("low", createJsonArray(chart.getLow()))
                .add("open", createJsonArray(chart.getOpen()))
                .add("close", createJsonArray(chart.getClose()))
                .build();
                System.out.println(">>>resp: " + resp);
                System.out.println(">>>Sending back to server>>>");
                return ResponseEntity.ok(resp.toString());
        }

        // Handle the case when the Optional is empty
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body("Stock information not available for the provided symbol.");
            
    }


    private JsonArray createJsonArray(Double[] values) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (double value : values) {
            arrayBuilder.add(value);
        }
        return arrayBuilder.build();
    }

    private JsonArray createJsonArray(String[] values) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (String value : values) {
            arrayBuilder.add(value);
        }
        return arrayBuilder.build();
    }



}
