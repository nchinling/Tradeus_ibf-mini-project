package sg.edu.nus.iss.stocktrackerbackend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.client.WebSocketClient;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import java.net.URI;

import java.util.ArrayList;
import java.util.List;


// working
@Service
public class WebSocketService {

    @Value("${twelve.data.websocket.key}")
    private String twelveDataWebSocketApiKey;

    private final List<String> receivedData = new ArrayList<>();

    public void sendReceivedData(String data) {
        receivedData.add(data);
    }

    public List<String> getReceivedData() {
        return receivedData;
    }

    public void clearReceivedData() {
    receivedData.clear();
    }

    public void getWebSocketData() throws Exception {
        // List<WebSocketStock> receivedData = new ArrayList<>();
       

        String ENDPOINT = "wss://ws.twelvedata.com/v1/quotes/price?apikey=" + twelveDataWebSocketApiKey;
        System.out.println(">>>The key in getWebSocketData is >>>>>" + twelveDataWebSocketApiKey);

        WebSocketClient client = null;

            client = new WebSocketClient(new URI(ENDPOINT)) {
                @Override
                public void onOpen(ServerHandshake handshake) {
                    System.out.println("TDWebSocket opened!");
                    // send("{\"action\": \"subscribe\", \"params\":{\"symbols\": \"D05:SGX,INFY:NSE,7203:JPX,002594:SZSE,ADYEN:Euronext,BT.A:LSE\"}}");
                    send("{\"action\": \"subscribe\", \"params\":{\"symbols\": \"7203,D05:SGX,INFY:NSE,2603:TWSE,002594,005930,AAPL,QQQ\"}}");
                  


            
                }

                @Override
                public void onMessage(String message) {
                    JSONParser parser = new JSONParser();
                    try {
                        JSONObject json = (JSONObject) parser.parse(message);
                        // System.out.println(json);
                        sendReceivedData(json.toString());
                    } catch(ParseException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onClose(int status, String reason, boolean remote) {
                    System.out.println("TDWebSocket closed. Status: " + status + ", Reason: " + reason);
                    this.close();
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            };

        
            client.connectBlocking();


    }

    
}





        








