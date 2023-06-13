package sg.edu.nus.iss.stocktrackerbackend.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import sg.edu.nus.iss.stocktrackerbackend.models.Stock;

@Service
public class StockService {

    @Value("${twelve.data.url}")
    private String twelveDataUrl;

    @Value("${twelve.data.key}")
    private String twelveDataApiKey;

    @Value("${twelve.data.host}")
    private String twelveDataApiHost;
    
    //function to get info from an external server using API.
   public Optional<Stock> getStockData(String symbol, String interval)
   throws IOException{
       System.out.println("twelveDataUrl: " + twelveDataUrl);
       System.out.println("twelveDataApiKey: " + twelveDataApiKey);
       System.out.println("twelveDataApiHost: " + twelveDataApiHost);
   
       String stockUrl = UriComponentsBuilder
                           .fromUriString(twelveDataUrl)
                           .queryParam("symbol", symbol)
                           .queryParam("interval", interval)
                           .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Key", twelveDataApiKey);
        headers.set("X-RapidAPI-Host", twelveDataApiHost);


        RequestEntity req = RequestEntity.get(stockUrl)
                    .headers(headers)
                    .build();

       RestTemplate template= new RestTemplate();
        ResponseEntity<String> r  = template.exchange(req, 
        String.class);


       //r.getBody is a string response from api.
        System.out.println(">>>managed to exchange: >>>>" + r.getBody());
       Stock s = Stock.createUserObject(r.getBody());
       String apiSymbol = s.getSymbol();
       String name = s.getName();

       System.out.println(">>>apiSymbol: " + apiSymbol);
       System.out.println(">>>name: " + name);
       return Optional.of(s);

   }
}
