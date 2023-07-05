package sg.edu.nus.iss.stocktrackerbackend.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.stocktrackerbackend.models.Notifications;
import sg.edu.nus.iss.stocktrackerbackend.services.WebSocketService;


@RestController
@CrossOrigin(origins="*")
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private WebSocketService webSocketService;

    // Initialize Notifications
    // private Notifications notifications = new Notifications(0);

    
    @MessageMapping("/notify")
    // @SendTo("/topic/notification")
    public List<String> getNotification() throws Exception {
        // Retrieve the initial stock data
        webSocketService.getWebSocketData();
        // List<String> stockData = webSocketService.getReceivedData();

        while (true) {
            // Perform necessary operations to fetch updated stock data
             List<String> stockData = webSocketService.getReceivedData();
            // System.out.println("I am inside websocket");
            template.convertAndSend("/topic/notification", stockData);
            webSocketService.clearReceivedData();
            // Push the updated stock data to the front-end
            Thread.sleep(3000);
            // Add appropriate delay if needed
        }
    }

    // @GetMapping("/notify")
    // public String getNotification() throws Exception {

    //     webSocketService.getWebSocketData();

    //     List<String> stockData = webSocketService.getReceivedData();
        

    //     // Increment Notification by one
    //     notifications.increment();

    //     System.out.println("Sending back to client the number:" + notifications.getCount());

    //     // Push notifications to front-end
    //     template.convertAndSend("/topic/notification", stockData);

    //     // return "Notifications successfully sent to Angular !";
    //     return "Successfully sent back to angular";
    // }

}




// working manual increment
// @RestController
// @CrossOrigin(origins="*")
// public class WebSocketController {

//     @Autowired
//     private SimpMessagingTemplate template;

//     // Initialize Notifications
//     private Notifications notifications = new Notifications(0);

//     @GetMapping("/notify")
//     public String getNotification() {

//         // Increment Notification by one
//         notifications.increment();

//         System.out.println("Sending back to client the number:" + notifications.getCount());

//         // Push notifications to front-end
//         template.convertAndSend("/topic/notification", notifications);

//         // return "Notifications successfully sent to Angular !";
//         return "Successfully sent back to angular";
//     }


// }




// @Controller
// @RequestMapping(path="/api", produces = MediaType.APPLICATION_JSON_VALUE)
// @CrossOrigin(origins="*")
// public class WebSocketController {

// @Autowired
// private WebSocketService webSocketService;


// @GetMapping(path = "/quote/websocket")
// @ResponseBody
// public ResponseEntity<String> getStockData() throws Exception {
//     System.out.println("I am inside websocketController");

//     // try {
//         List<WebSocketStock> receivedData = webSocketService.getWebSocketData();

//         for (WebSocketStock stock : receivedData) {
//             System.out.println(">>>>The websocket stock is>>>>>>" +stock);
//         }
//         System.out.println(receivedData);

//         JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

//     for (WebSocketStock stock : receivedData) {

//     JsonObjectBuilder stockBuilder = Json.createObjectBuilder()
//         .add("symbol", stock.getSymbol() != null ? stock.getSymbol() : "AAPL")
//         .add("price", stock.getPrice() != null ? stock.getPrice() : 100)
//         .add("exchange", stock.getExchange() != null ? stock.getExchange() : "Nasdaq")
//         .add("currency", stock.getCurrency() != null ? stock.getCurrency() : "USD")
//         .add("ask", stock.getAsk() != null ? stock.getAsk() : 0)
//         .add("volume", stock.getDay_volume() != null ? stock.getDay_volume() : 0)
//         .add("bid", stock.getBid() != null ? stock.getBid() : 0);

//         arrayBuilder.add(stockBuilder);
        
//     }

//         JsonArray respArray = arrayBuilder.build();
//         System.out.println(">>>sending back websocket data.>>>>>Hooray: " + respArray);
//         return ResponseEntity.ok(respArray.toString());

    
//     }

// }



