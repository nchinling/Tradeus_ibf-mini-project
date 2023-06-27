package sg.edu.nus.iss.stocktrackerbackend.services;


import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import sg.edu.nus.iss.stocktrackerbackend.models.WebSocketStock;

import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.WebSocketAdapter;
import org.java_websocket.client.WebSocketClient;
 
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Consumer;


import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Consumer;


// @Service
// public class WebSocketService {
    
    
//     @Value("${twelve.data.websocket.key}")
//     private String twelveDataWebSocketApiKey;

//     // private List<WebSocketStock> receivedData = new ArrayList<>();


//     public List<WebSocketStock> getWebSocketData() throws Exception {

//         List<WebSocketStock> receivedData = new ArrayList<>();

      
//         String ENDPOINT = "wss://ws.twelvedata.com/v1/quotes/price?apikey=" + twelveDataWebSocketApiKey;
//         System.out.println(">>>The key is >>>>>" + twelveDataWebSocketApiKey);
//         WebSocketClient client = new WebSocketClient(new URI(ENDPOINT)) {
//             @Override
//             public void onOpen(ServerHandshake handshake) {
//                 System.out.println("TDWebSocket opened!");
//                 // send("{\"action\": \"subscribe\", \"params\":{\"symbols\": \"EUR/USD,BTC/USD, D05:SGX,INFY:NSE,7203:JPX,002594:SZSE\"}}");
//                 send("{\"action\": \"subscribe\", \"params\":{\"symbols\": \"D05:SGX,INFY:NSE,7203:JPX,002594:SZSE\"}}");
            

//             }

//             @Override
//             public void onMessage(String message) {
//                 //  WebSocketStock webSocketStock=null;
//                 try {
//                     // List<WebSocketStock> receivedData = new ArrayList<>();
//                     ObjectMapper objectMapper = new ObjectMapper();
//                     WebSocketStock webSocketStock = objectMapper.readValue(message, WebSocketStock.class);
//                     receivedData.add(webSocketStock);
//                     System.out.println(webSocketStock);
//                 } catch (IOException e) {
//                     e.printStackTrace();
//                 }
//             }


 
//             // @Override
//             // public void onMessage(String message) {
//             //     JSONParser parser = new JSONParser();
//             //     try {
//             //         JSONObject json = (JSONObject) parser.parse(message);
//             //         System.out.println(json);
//             //     } catch(ParseException e) {
//             //         e.printStackTrace();
//             //     }
//             // }

            
//             @Override
//             public void onClose(int status, String reason, boolean remote) {
//                 System.out.println("TDWebSocket closed. Status: " + status + ", Reason: " + reason);
//                  this.close();
//             }
 
//             @Override
//             public void onError(Exception e) {
//                 e.printStackTrace();
//             }
//         };

//         // return receivedData;

//         client.connectBlocking();


//             // public List<WebSocketStock> getReceivedData() {
//         return receivedData;
//     // }
//     }

// }


// working
@Service
public class WebSocketService {

    @Value("${twelve.data.websocket.key}")
    private String twelveDataWebSocketApiKey;

    public List<WebSocketStock> getWebSocketData() throws Exception {
        List<WebSocketStock> receivedData = new ArrayList<>();

        String ENDPOINT = "wss://ws.twelvedata.com/v1/quotes/price?apikey=" + twelveDataWebSocketApiKey;
        System.out.println(">>>The key is >>>>>" + twelveDataWebSocketApiKey);

        WebSocketClient client = null;
        try {
            client = new WebSocketClient(new URI(ENDPOINT)) {
                @Override
                public void onOpen(ServerHandshake handshake) {
                    System.out.println("TDWebSocket opened!");
                    send("{\"action\": \"subscribe\", \"params\":{\"symbols\": \"D05:SGX,INFY:NSE,7203:JPX,002594:SZSE,ADYEN:Euronext,BT.A:LSE\"}}");
                }

                @Override
                public void onMessage(String message) {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        WebSocketStock webSocketStock = objectMapper.readValue(message, WebSocketStock.class);
                        receivedData.add(webSocketStock);
                        System.out.println(webSocketStock);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onClose(int status, String reason, boolean remote) {
                    System.out.println("TDWebSocket closed. Status: " + status + ", Reason: " + reason);
                    synchronized (receivedData) {
                        receivedData.notify();  // Notify the waiting thread
                    }
                    this.close();
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            };

            final WebSocketClient finalClient = client;
            Thread thread = new Thread(() -> {
                try {
                    finalClient.connectBlocking();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });


            thread.start();  // Start the WebSocket connection in a separate thread

            synchronized (receivedData) {
                receivedData.wait();  // Wait for the WebSocket connection to close
            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return receivedData;
    }
    
}



        








