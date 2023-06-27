package sg.edu.nus.iss.stocktrackerbackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import sg.edu.nus.iss.stocktrackerbackend.services.WebSocketService;

@SpringBootApplication
public class StocktrackerBackendApplication {

	// @Autowired
    // private WebSocketService webSocketService;

	public static void main(String[] args) {
		SpringApplication.run(StocktrackerBackendApplication.class, args);
	}

	// @EventListener(ApplicationReadyEvent.class)
    // public void startWebSocketConnection() {
    //     try {
    //         webSocketService.getWebSocketData();
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }

}
