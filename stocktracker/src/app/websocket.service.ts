import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { Observable, tap, map, Subject } from "rxjs";
import { WebSocketStock} from "./models";
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';

const URL_API_TRADE_SERVER = 'http://localhost:8080/api'


@Injectable()
export class WebSocketService {

    http = inject(HttpClient)
  
    onWebSocketDataRequest = new Subject<WebSocketStock[]>()

    
  // getWebSocketData(): Observable<WebSocketStock[]> {
  
  //   return this.http.get<WebSocketStock[]>(`${URL_API_TRADE_SERVER}/quote/websocket`)
  //     .pipe(
  //       // tap(resp => this.onAnnualisedPortfolioDataRequest.next(resp)),
  //       tap(response => console.log('Response received:', response)),
  //       tap(response => this.onWebSocketDataRequest.next(response)),
  //       map(webSocketList => webSocketList.map(webSocketItem => ({
  //         symbol: webSocketItem.symbol, price: webSocketItem.price, exchange: webSocketItem.exchange, currency:webSocketItem.currency, 
  //          ask: webSocketItem.ask, volume: webSocketItem.volume, bid: webSocketItem.bid
  //       })))
  //     );
  // }

  
}



  // private String symbol;
  // private String exchange;
  // private String currency;
  // private Double price;
  // private Double ask;
  // private Double day_volume;
  // private Double bid;