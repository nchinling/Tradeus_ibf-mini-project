package sg.edu.nus.iss.stocktrackerbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;



@Configuration
@EnableWebSocketMessageBroker
// @CrossOrigin(origins="*")
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer  {
	@Override
	public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
		// stompEndpointRegistry.addEndpoint("/socket")
		// 		.setAllowedOrigins("http://localhost:4200")
		// 		.withSockJS();
		stompEndpointRegistry.addEndpoint("/api/socket")
		// .setAllowedOrigins("*")
		.withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/topic");
		registry.setApplicationDestinationPrefixes("/app");
	}

	//for ng build deployment
    // @Override
    // public void addResourceHandlers(ResourceHandlerRegistry registry) {
    //     registry.addResourceHandler("/**")
    //             .addResourceLocations("classpath:/static/")
    //             .setCachePeriod(0);
    // }
	
}
