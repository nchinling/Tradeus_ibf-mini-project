import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { Subject, lastValueFrom, tap, map, interval } from "rxjs";
import { Market, MarketIndex, Stock } from "./models";

const URL_API_TRADE_SERVER = 'http://localhost:8080/api/quote'

@Injectable()
export class StockService {

    http = inject(HttpClient)

    onStockRequest = new Subject<Stock>()
    onMarketRequest = new Subject<MarketIndex>()

    getStockData(symbol:string, interval:string): Promise<Stock> {

        const queryParams = new HttpParams()
            .set('symbol', symbol)
            .set('interval', interval)
        console.info('>>>>>>sending to Stock server...')
        return lastValueFrom(
          this.http.get<Stock>(`${URL_API_TRADE_SERVER}/stock`, { params: queryParams })
            .pipe(
              tap(resp => this.onStockRequest.next(resp)),
              map(resp => ({ symbol: resp.symbol, name: resp.name, 
                          exchange: resp.exchange, currency: resp.currency,
                          open:resp.open, high:resp.high, low:resp.low,
                          close:resp.close, volume:resp.volume, 
                          previous_close:resp.previous_close, change:resp.change,
                          percent_change:resp.percent_change, datetime:resp.datetime
                          }))
            )
        )
    }

    // getMarketData(symbol:string, interval:string): Promise<MarketIndex> {

    //     const queryParams = new HttpParams()
    //         .set('symbol', symbol)
    //         .set('interval', interval)
    //     console.info('>>>>>>sending to Stock server...')
    //     return lastValueFrom(
    //       this.http.get<MarketIndex>(`${URL_API_TRADE_SERVER}/market`, { params: queryParams })
    //         .pipe(
    //           tap(resp => this.onMarketRequest.next(resp)),
    //           map(resp => ({ symbol: resp.symbol, name: resp.name, close:resp.close, 
    //                       percentage_change:resp.percentage_change, 
    //                       change:resp.change, datetime:resp.datetime
    //                       }))
    //         )
    //     )
    // }

    getMarketData(markets: Market[]): Promise<MarketIndex[]> {
        const marketRequests: Promise<MarketIndex>[] = [];
      
        for (const market of markets) {
          const queryParams = new HttpParams()
            .set('symbol', market.symbol)
            .set('interval', market.interval);
      
          const request=lastValueFrom(this.http.get<MarketIndex>(`${URL_API_TRADE_SERVER}/market`, { params: queryParams })
            .pipe(
              tap(resp => this.onMarketRequest.next(resp)),
              map(resp => ({
                symbol: resp.symbol,
                name: resp.name,
                close: resp.close,
                percentage_change: resp.percentage_change,
                change: resp.change,
                datetime: resp.datetime
              }))
            ))
        
      
          marketRequests.push(request);
        }
      
        console.info('>>>>>>sending to Stock server...');
        // return Promise.all(marketRequests);
        return Promise.all(marketRequests);

      }

}



