package sg.edu.nus.iss.stocktrackerbackend.repositories;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.stocktrackerbackend.models.Market;
import sg.edu.nus.iss.stocktrackerbackend.models.Stock;
import sg.edu.nus.iss.stocktrackerbackend.models.StockInfo;

@Repository    
public class StockRepository {
    
       
      //autowired in a bean.
      @Autowired @Qualifier("marketbean")
      private RedisTemplate<String, String> redisTemplate;

      @Autowired
	    private MongoTemplate mongoTemplate;


    public void saveMarketData(Market market){
      	//implement cookie of 5 minutes
        int cookieTime = 5;
        Duration duration = Duration.ofMinutes(cookieTime);
        System.out.println(">>>>>>weather.getCity>>>"+ market.getSymbol());
        this.redisTemplate.opsForValue().set(market.getSymbol(), market.toJSON().toString(), duration);
    }

    public void saveStockData(Stock stock, String interval){
      	
      int cookieTime = convertIntervalToMinutes(interval);
      

        Duration duration = Duration.ofMinutes(cookieTime);
        String key = stock.getSymbol() + interval;
        System.out.println(">>>>>>stock.getSymbol>>>"+ stock.getSymbol());
        this.redisTemplate.opsForValue().set(key, stock.toJSON().toString(), duration);
    }

    public Optional<Market> getMarketFromRedis(String symbol) throws IOException{
      System.out.println(">>>>>getMarketFromRedis market>>>"+ symbol);

        String json = redisTemplate.opsForValue().get(symbol);
        System.out.println(">>>>>>>> json from Redis>>>>>>"+json);
        if(null == json|| json.trim().length() <= 0){
          System.out.println(">>>>>>>> I am returning an empty object");
            return Optional.empty();
        }

        System.out.println(">>>>>>>>Data retrieved from Redis Server>>>>>" );

        return Optional.of(Market.createUserObjectFromRedis(json));
  
    }

    public Optional<Stock> getStockFromRedis(String symbol, String interval) throws IOException{
      System.out.println(">>>>>getStockFromRedis stock>>>"+ symbol);

        String key = symbol+interval;
        String json = redisTemplate.opsForValue().get(key);
        System.out.println(">>>>>>>> json from Redis>>>>>>"+json);
        if(null == json|| json.trim().length() <= 0){
          System.out.println(">>>>>>>> I am returning an empty object");
            return Optional.empty();
        }

        System.out.println(">>>>>>>>Data retrieved from Redis Server>>>>>" );

        return Optional.of(Stock.createUserObjectFromRedis(json));
  

    }


	/*
	 *  db.nyse.find({ name: { $regex: "filter", $options: "i" } })
	 * 	.sort({ name: 1 })
	 * 	.skip(skip)
	 * 	.limit(limit)
	 * 	.projection({ _id: 0, name: 1, symbol:1, currency:1,exchange:1,country:1,type:1 })
	 */
	public List<StockInfo> getStocksList(String exchange, String filter, int skip, int limit) {

		Criteria criteria = Criteria.where("name").regex(filter, "i");

		Query query = new Query(criteria)
			.with(Sort.by(Direction.ASC, "name"))
			.skip(skip)
			.limit(limit);
		query.fields()
			.exclude("_id")
			.include("name", "symbol","currency", "exchange","country","type");

		return mongoTemplate.find(query, Document.class, exchange).stream()
			.map(doc -> 
					new StockInfo(doc.getString("name"),doc.getString("symbol"),doc.getString("currency"),
                        doc.getString("exchange"),doc.getString("country"),doc.getString("type") ))
			.toList();
	}

  private Integer convertIntervalToMinutes(String interval){
    int cookieTime = 0;
    switch (interval) {
          case "1hr":
              cookieTime = 1 * 60;
              break;
          case "1day":
              cookieTime = cookieTime * 60 * 24;
              break;
          case "1min":
              cookieTime = 1;
              break;
          case "5min":
              cookieTime = 5;
              break;
          default:
              cookieTime = 15;
              break;
      }
    return cookieTime;
  }







}
