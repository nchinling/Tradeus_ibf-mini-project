package sg.edu.nus.iss.stocktrackerbackend.services;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import sg.edu.nus.iss.stocktrackerbackend.models.Chart;
import sg.edu.nus.iss.stocktrackerbackend.models.Stock;
import sg.edu.nus.iss.stocktrackerbackend.repositories.StockRepository;

@Service
public class ChartService {
    
    
    @Value("${twelve.data.url}")
    private String twelveDataUrl;

    @Value("${twelve.data.key}")
    private String twelveDataApiKey;

    @Value("${twelve.data.host}")
    private String twelveDataApiHost;

    // @Autowired
    // private StockRepository stockRepo;
    
    //function to get info from an external server using API.
   public Optional<Chart> getTimeSeries(String symbol, String interval, String dataPoints)
   throws IOException{
       System.out.println("twelveDataUrl: " + twelveDataUrl);
       System.out.println("twelveDataApiKey: " + twelveDataApiKey);
       System.out.println("twelveDataApiHost: " + twelveDataApiHost);
   
       String stockUrl = UriComponentsBuilder
                           .fromUriString(twelveDataUrl+"/time_series")
                           .queryParam("symbol", symbol)
                           .queryParam("interval", interval)
                           .queryParam("outputsize", dataPoints)
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
        Chart c = Chart.createUserObject(r.getBody());

       //for debugging
       String receivedSymbol = c.getSymbol();
       String receivedInterval = c.getInterval();
       System.out.println(">>>Symbol: " + receivedSymbol);
       System.out.println(">>>name: " + receivedInterval);

       return Optional.of(c);

   }
}
