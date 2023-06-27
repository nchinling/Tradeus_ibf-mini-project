package sg.edu.nus.iss.stocktrackerbackend.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;

import sg.edu.nus.iss.stocktrackerbackend.models.WebSocketStock;
import sg.edu.nus.iss.stocktrackerbackend.services.WebSocketService;



@Controller
@RequestMapping(path="/api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins="*")
public class WebSocketController {

@Autowired
private WebSocketService webSocketService;


@GetMapping(path = "/quote/websocket")
@ResponseBody
public ResponseEntity<String> getStockData() throws Exception {
    System.out.println("I am inside websocketController");

    // try {
        List<WebSocketStock> receivedData = webSocketService.getWebSocketData();

        for (WebSocketStock stock : receivedData) {
            System.out.println(">>>>The websocket stock is>>>>>>" +stock);
        }
        System.out.println(receivedData);

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

    for (WebSocketStock stock : receivedData) {

    JsonObjectBuilder stockBuilder = Json.createObjectBuilder()
        .add("symbol", stock.getSymbol() != null ? stock.getSymbol() : "AAPL")
        .add("price", stock.getPrice() != null ? stock.getPrice() : 100)
        .add("exchange", stock.getExchange() != null ? stock.getExchange() : "Nasdaq")
        .add("currency", stock.getCurrency() != null ? stock.getCurrency() : "USD")
        .add("ask", stock.getAsk() != null ? stock.getAsk() : 0)
        .add("volume", stock.getDay_volume() != null ? stock.getDay_volume() : 0)
        .add("bid", stock.getBid() != null ? stock.getBid() : 0);

        arrayBuilder.add(stockBuilder);
        
    }

        JsonArray respArray = arrayBuilder.build();
        System.out.println(">>>sending back websocket data.>>>>>Hooray: " + respArray);
        return ResponseEntity.ok(respArray.toString());

    
    }

}



