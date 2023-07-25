package sg.edu.nus.iss.stocktrackerbackend.controllers;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;
import sg.edu.nus.iss.stocktrackerbackend.services.WebSocketService;


@RestController
// @CrossOrigin(origins="*")
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private WebSocketService webSocketService;

   
    @MessageMapping("/notify")
    // @SendTo("/topic/notification")
    public List<String> getNotification() throws Exception {
        // Retrieve the initial stock data
        webSocketService.getWebSocketData();
        // List<String> stockData = webSocketService.getReceivedData();

        while (true) {
     
             List<String> stockData = webSocketService.getReceivedData();
            // System.out.println("I am inside websocket");
            template.convertAndSend("/topic/notification", stockData);
            webSocketService.clearReceivedData();
          
            Thread.sleep(3000);
           
        }
    }


}



