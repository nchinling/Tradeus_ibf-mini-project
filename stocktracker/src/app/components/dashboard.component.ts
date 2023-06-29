import { Component, HostListener, Injectable, Input, OnChanges, OnInit, inject } from '@angular/core';
import { BehaviorSubject, Observable, Subject, Subscription, filter, interval, map } from 'rxjs';
import { AccountService } from '../account.service';
import { LoginResponse, MarketIndex, RegisterResponse, Stock, Market, StockInfo, PortfolioData, AnnualisedPortfolioData, WebSocketStock } from '../models';
import { ActivatedRoute, NavigationStart, Router } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { StockService } from '../stock.service';
import { WebSocketService } from '../websocket.service';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';


@Injectable()
@Component({
  selector: 'app-dashboard', 
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent implements OnInit, OnChanges{


  stockSvc = inject(StockService)
  accountSvc = inject(AccountService)
  // webSocketService = inject(WebSocketService)
  activatedRoute = inject(ActivatedRoute)
  router = inject(Router)
  title = inject(Title)
  http=inject(HttpClient)

  public notifications = 0;

// constructor(private webSocketService: WebSocketService) {
//   this.getNotification()
//   let stompClient = this.webSocketService.connect();

//   stompClient.connect({}, (frame: any) => {
//     stompClient.subscribe('/topic/notification', (notifications: any) => {
//       this.notifications = JSON.parse(notifications.body).count;
//     });
//   });

// }

  loginResponse$!: Observable<LoginResponse>
  registerResponse$!: Observable<RegisterResponse>
  errorMessage$!: Observable<string>
  username!: string
  parsedUsername!: string
  status!: string
  timestamp!: string
  accountId!: string
  key!: string

  markets: Market[] = [
    { symbol: "SPX", interval: "1day" },
    { symbol: "VIX", interval: "1day" },
    { symbol: "IXIC", interval: "1day" },
    { symbol: "DJI", interval: "1day" },
    { symbol: "RUT", interval: "1day" }
  ];

  snp500: string[ ] = [
    'AAPL', 'MSFT', 'AMZN', 'NVDA' , 'GOOGL', 'TSLA', 'GOOG', 'META', 'BRK.B', 'XOM'
  ];

  nasdaq: string[] = [
    'MSFT', 'AAPL', 'NVDA', 'AMZN', 'TSLA', 'META', 'GOOGL', 'GOOG', 'AVGO', 'PEP'
  ];

  marketIndex$!: Promise<MarketIndex[]>

  //for watchlist
  stockSymbol$!: Observable<string>
  symbols$!:Promise<string[]>
  symbols!:string[]
  symbol!:string
  watchList$!: Promise<Stock[]>
  indexSnP$!: Promise<Stock[]>
  indexNasdaq$!: Promise<Stock[]>
  // webSocketSymbols = ['AAPL', 'QQQ', 'ABML', 'BT.A' ];
  webSocketSymbols = ['D05:SGX','INFY:NSE', '2603:TWSE', '7203', '002594', '005930', 'AAPL', 'QQQ'];


  portfolioSymbols$!:Promise<string[]>
  portfolioData$!:Promise<PortfolioData[]>
  annualisedPortfolioData$!:Observable<AnnualisedPortfolioData[]>
  webSocketStocks$!:Observable<WebSocketStock[]>
  socket!: WebSocket;
  webSocketStocks: WebSocketStock[] = [];
  ENDPOINT!: string


  onStockRequest = new Subject<string>()

  //for ng-bootstrap nav
  active = 1;



 ngOnInit():void{
    this.loginResponse$ = this.accountSvc.onLoginRequest
    this.registerResponse$ = this.accountSvc.onRegisterRequest
    this.errorMessage$ = this.accountSvc.onErrorMessage

        // Access the query parameters
        const queryParams = this.activatedRoute.snapshot.queryParams;
        
        // Example: Access specific query parameters
        this.status = queryParams['status'];
        this.timestamp = queryParams['timestamp'];
        this.accountId = queryParams['account_id'];
        this.username = queryParams['username'];
     

        this.title.setTitle(`Account: ${this.accountSvc.username}`)

        console.log('Status:', this.status);
        console.log('Timestamp:', this.timestamp);
        console.log('Account ID:', this.accountId);
        console.log('Username:', this.username);

        if(localStorage.getItem('username')){
          this.username = this.accountSvc.username
          this.accountId = this.accountSvc.account_id
        } else{
        }

        this.key = this.accountSvc.key

        if (this.key) {
          console.info('hello, there is a key' + this.key)
          this.connectWebSocket(this.key);
        }


      this.annualisedPortfolioData$ = this.stockSvc.getAnnualisedPortfolioData(this.accountId);

      //initialise portfolio (cumulative)
      this.portfolioSymbols$ = this.stockSvc.getPortfolioSymbols(this.accountId)
      console.info('this.symbols$ is' + this.portfolioSymbols$)
  
      this.portfolioSymbols$.then((symbol: string[]) => {
        console.info('Symbols:', symbol);
        this.portfolioData$ = this.stockSvc.getPortfolioData(symbol, this.accountId);
      }).catch((error) => {
        console.error(error);
      });

      //initialise portfolio (annualised)

      //load market data
      // this.marketIndex$ = this.stockSvc.getMarketData('SPX', '1day');
      this.marketIndex$ = this.stockSvc.getMarketData(this.markets)
      this.indexSnP$ = this.stockSvc.getStocklistData(this.snp500)
      this.indexNasdaq$ = this.stockSvc.getStocklistData(this.nasdaq)
      this.stockSymbol$ = this.stockSvc.onStockSelection
      console.info('I am in dashboard')
      this.symbol=this.stockSvc.symbol
      this.symbols = this.stockSvc.symbols
      console.info('this.symbols in ngOnInit are:' + this.symbols)
      this.symbols$ = this.stockSvc.getWatchlist(this.username)
      console.info('this.symbols$ is' + this.symbols$)
      // this.watchList$ = this.stockSvc.getWatchlistData(this.symbols);
      // this.symbols$ = this.stockSvc.getWatchlist(this.username);

      this.symbols$.then((symbol: string[]) => {
        console.info('Symbols:', symbol);
        this.watchList$ = this.stockSvc.getStocklistData(symbol);
      }).catch((error) => {
        console.error(error);
      });

}

getNotification(): void {
  this.http.get('http://localhost:8080/notify', {responseType: 'text'}).subscribe({
    next: (response) => {
      console.log('Notification:', response);
      // Handle the notification data here
    },
    error: (error) => {
      if (error instanceof HttpErrorResponse) {
        console.error('Failed to retrieve notification', error.status, error.statusText);
        console.log('Error body:', error.error);
      } else {
        console.error('An unexpected error occurred:', error);
      }
    }
  });
}



private initialiseWebSocketStocks(aliases: { [key: string]: string }) {
  this.webSocketStocks = this.webSocketSymbols.map(symbol => ({
    symbol:aliases[symbol] || symbol,
    exchange: '',
    currency: '',
    price: 0,
    ask: 0,
    bid: 0,
    volume: 0,
    volumeChanged: false,
    priceChanged: false,
    askChanged: false,
    bidChanged: false,
    previousVolume: 0,
    previousPrice: 0,
    previousAsk: 0,
    previousBid: 0
  }));
}

  private connectWebSocket(key:string) {
    this.ENDPOINT= 'wss://ws.twelvedata.com/v1/quotes/price?apikey='+key;
    // this.ENDPOINT= 'wss://ws.twelvedata.com/v1/quotes/price?apikey='
    this.socket = new WebSocket(this.ENDPOINT);

    this.socket.onopen = (event) => {
      console.log('WebSocket opened!');
      // const symbols = ['AAPL', 'QQQ', 'ABML', 'TRP:TSX'];
      // this.socket.send(JSON.stringify({ action: 'subscribe', params: { symbols: 'AAPL', } }));
      this.socket.send(JSON.stringify({ action: 'subscribe', params: { symbols: this.webSocketSymbols.join(',') } }));
    };

    // this.socket.onmessage = (event) => {
    //   const message = JSON.parse(event.data) as WebSocketStock
    //   console.log(message);
    //   this.messages.push(message);
    // };

    const symbolAliases:{ [key: string]: string } = {
      'INFY': 'Infosys',
      'INFY:NSE': 'Infosys',
      '7203': 'Toyota',
      '002594': 'BYD',
      '005930': 'Samsung',
      'D05': 'DBS Group',
      'D05:SGX': 'DBS Group',
      'QQQ': 'Invesco',
      '2603:TWSE': 'Evergreen',
      '2603': 'Evergreen',
      'AAPL':'Apple'
    };

    this.initialiseWebSocketStocks(symbolAliases);


    this.socket.onmessage = (event) => {
      const messageString = event.data;
      const message = JSON.parse(messageString);

      const symbol = message.symbol;
      const alias = symbolAliases[symbol] || symbol; 

      const existingStock = this.webSocketStocks.find(stock => stock.symbol === alias);
      let previousVolume = 0;
      let previousPrice = 0;
      let previousAsk = 0;
      let previousBid = 0;
    
      if (existingStock) {
        previousVolume = existingStock.volume;
        previousPrice = existingStock.price;
        previousAsk = existingStock.ask;
        previousBid = existingStock.bid;
      }
    
      const webSocketStock: WebSocketStock = {
        symbol: alias,
        exchange: message.exchange,
        currency: message.currency,
        price: message.price,
        ask: message.ask,
        bid: message.bid,
        volume: message.day_volume,
        volumeChanged: previousVolume !== message.day_volume,
        priceChanged: previousPrice !== message.price,
        askChanged: previousAsk !== message.ask,
        bidChanged: previousBid !== message.bid,
        previousVolume,
        previousPrice,
        previousAsk,
        previousBid

      };
      console.log(webSocketStock);
      // this.webSocketStocks.push(webSocketStock);
        // Find the index of the existing stock with the same symbol
        const index = this.webSocketStocks.findIndex(stock => stock.symbol === webSocketStock.symbol);

        if (index !== -1) {
          // Replace the existing stock with the new data
          this.webSocketStocks[index] = webSocketStock;
        } else {
          // Add the new stock to the array
          this.webSocketStocks.push(webSocketStock);
        }
      }


    this.socket.onclose = (event) => {
      console.log('WebSocket closed.');
    };

    this.socket.onerror = (error) => {
      console.error('WebSocket error:', error);
    };
  }



  ngAfterViewInit():void{

  }

  ngOnChanges(): void{

  }


  viewStock(symbol:string){

    console.info('Printed the symbol:'+ symbol)
    this.stockSvc.symbol = symbol
    // this.router.navigate(['research']);

    // Create a Promise that resolves when this.stockSvc.symbol has completed
    const symbolPromise = new Promise((resolve) => {
      resolve(this.stockSvc.symbol);
    });

    // Wait for the symbolPromise to resolve before navigating to "research" route
    symbolPromise.then(() => {
      this.router.navigate(['research']);
    });


  }


  removeFromWatchlist(index: number) {
    const symbolToRemove: string = this.stockSvc.symbols[index];
    console.info('To remove symbol: ' + symbolToRemove);
  
    this.stockSvc.removeFromWatchlist(index, this.username)
      .then(() => {
        console.info('Symbol removed successfully');
        return this.stockSvc.getWatchlist(this.username);
      })
      .then((symbol: string[]) => {
        console.info('The updated list of Symbols after removal are:', symbol);
        this.symbols$ = Promise.resolve(symbol);
        return this.stockSvc.getStocklistData(symbol);
      })
      .then((stocks: Stock[]) => {
        this.watchList$ = Promise.resolve(stocks);
      })
      .catch((error) => {
        console.error(error);
      });
  }


  calculateTotalReturn(portfolioData: any[]): number {
    return portfolioData.reduce((total, data) => total + data.total_return, 0);
  }

  calculateTotalPercentageReturn(portfolioData: any[]): number {
    const totalReturn = this.calculateTotalReturn(portfolioData);
    const totalInvestment = portfolioData.reduce((total, data) => total + data.buy_total_price, 0);
    return (totalReturn / totalInvestment) * 100;
  }

  // ngOnDestroy() {
  //   if (this.loginResponse$) {
  //     this.loginResponse$.unsubscribe();
  //   }
  // }

  
}
