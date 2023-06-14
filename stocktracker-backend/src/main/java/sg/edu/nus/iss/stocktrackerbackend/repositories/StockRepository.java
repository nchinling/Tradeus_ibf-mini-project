package sg.edu.nus.iss.stocktrackerbackend.repositories;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.stocktrackerbackend.models.Market;

@Repository    
public class StockRepository {
    
       
      //autowired in a bean.
      @Autowired @Qualifier("marketbean")

      private RedisTemplate<String, String> template;


    public void saveMarketData(Market market){
      	//implement cookie of 30 mins
        int cookieTime = 1;
        Duration duration = Duration.ofDays(cookieTime);
        System.out.println(">>>>>>weather.getCity>>>"+ market.getSymbol());
        this.template.opsForValue().set(market.getSymbol(), market.toJSON().toString(), duration);
    }

    public Optional<Market> getMarketFromRedis(String symbol) throws IOException{
      System.out.println(">>>>>getMarketFromRedis market>>>"+ symbol);

        String json = template.opsForValue().get(symbol);
        System.out.println(">>>>>>>> json from Redis>>>>>>"+json);
        if(null == json|| json.trim().length() <= 0){
          System.out.println(">>>>>>>> I am returning an empty object");
            return Optional.empty();
        }

        System.out.println(">>>>>>>>Data retrieved from Redis Server>>>>>" );

        return Optional.of(Market.createUserObjectFromRedis(json));
  

    }
}
