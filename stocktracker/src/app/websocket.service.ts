// declare var require: any
import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { Observable, tap, map, Subject, throwError, catchError } from "rxjs";
import { WebSocketStock} from "./models";
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';
import * as SockJs from 'sockjs-client';
import * as Stomp from 'stompjs';



//working web socket
@Injectable()
export class WebSocketService {

    // Open connection with the back-end socket
    public connect() {
        // let socket = new SockJs(`http://localhost:8080/socket`);
        let socket = new SockJs(`api/socket`);

        let stompClient = Stomp.over(socket);

        return stompClient;
    }

    
}

// @Injectable()
// export class WebSocketService {

//     http = inject(HttpClient)
  
//     onWebSocketDataRequest = new Subject<WebSocketStock[]>()

//     constructor() { }


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

  
// }





//to be used as submission codes
// @Injectable()
// export class WebSocketService {
//   socket!: WebSocketSubject<any>;

//   constructor() { }

//   connectWebSocket(): void {
//     this.socket = webSocket('ws://localhost:8080/ws');
//   }

//   getWebSocketData(): Observable<WebSocketStock[]> {
//     return this.socket.pipe(
//       catchError((error: any) => {
//         // Handle WebSocket errors
//         console.error('WebSocket error:', error);
//         return throwError(error);
//       })
//     )
//   }
// }







