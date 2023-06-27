import { Component, HostListener, Injectable, Input, OnChanges, OnInit, inject } from '@angular/core';
import { Observable, Subject, Subscription, interval, map } from 'rxjs';
import { AccountService } from '../account.service';
import { LoginResponse, MarketIndex, RegisterResponse, Stock, Market, StockInfo, PortfolioData, AnnualisedPortfolioData, WebSocketStock } from '../models';
import { ActivatedRoute, NavigationStart, Router } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { StockService } from '../stock.service';
import { WebSocketService } from '../websocket.service';


@Injectable()
@Component({
  selector: 'app-dashboard', 
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent implements OnInit, OnChanges{


  stockSvc = inject(StockService)
  accountSvc = inject(AccountService)
  webSocketSvc = inject(WebSocketService)
  activatedRoute = inject(ActivatedRoute)
  router = inject(Router)
  title = inject(Title)

  loginResponse$!: Observable<LoginResponse>
  registerResponse$!: Observable<RegisterResponse>
  errorMessage$!: Observable<string>
  username!: string
  parsedUsername!: string
  status!: string
  timestamp!: string
  accountId!: string

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
  webSocketSymbols = ['AAPL', 'QQQ', 'ABML', 'BT.A' ];

  portfolioSymbols$!:Promise<string[]>
  portfolioData$!:Promise<PortfolioData[]>
  annualisedPortfolioData$!:Observable<AnnualisedPortfolioData[]>
  webSocketStocks$!:Observable<WebSocketStock[]>
  socket!: WebSocket;
  webSocketStocks: WebSocketStock[] = [];
  ENDPOINT: string = 'wss://ws.twelvedata.com/v1/quotes/price?apikey=a875c7c23fba474fb03828f4557dcb97';


  onStockRequest = new Subject<string>()

  //for ng-bootstrap nav
  active = 1;



 ngOnInit():void{
    // this.loginResponse$ = this.accountSvc.onLoginRequest
    this.registerResponse$ = this.accountSvc.onRegisterRequest
    this.loginResponse$ = this.accountSvc.onLoginRequest
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
      

      this.connectWebSocket();
      // this.webSocketStocks$ = this.webSocketSvc.getWebSocketData();
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


      // this.webSocketStocks$ = this.webSocketSvc.getWebSocketData();

      // this.webSocketStocks$ = this.webSocketSvc.getWebSocketData()
      // .pipe(
      //   map(webSocketItem => [webSocketItem]) // Convert WebSocketStock to WebSocketStock[]
      // );

  }



private initialiseWebSocketStocks() {
  this.webSocketStocks = this.webSocketSymbols.map(symbol => ({
    symbol: symbol,
    exchange: '',
    currency: '',
    price: 0,
    ask: 0,
    bid: 0,
    volume: 0
  }));
}

  private connectWebSocket() {
    this.initialiseWebSocketStocks();
    
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

    this.socket.onmessage = (event) => {
      const messageString = event.data;
      const message = JSON.parse(messageString);
    
      const webSocketStock: WebSocketStock = {
        symbol: message.symbol,
        exchange: message.exchange,
        currency: message.currency,
        price: message.price,
        ask: message.ask,
        bid: message.bid,
        volume: message.day_volume
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

  
}
