import { Component, Injectable, OnChanges, OnInit, inject } from '@angular/core';
import { Observable, Subject} from 'rxjs';
import { AccountService } from '../account.service';
import { LoginResponse, MarketIndex, RegisterResponse, Stock, Market, PortfolioData, AnnualisedPortfolioData, WebSocketStock } from '../models';
import { ActivatedRoute, Router } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { StockService } from '../stock.service';
import { WebSocketService } from '../websocket.service';
import { HttpClient} from '@angular/common/http';


@Injectable()
@Component({
  selector: 'app-dashboard', 
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent implements OnInit, OnChanges{


  stockSvc = inject(StockService)
  accountSvc = inject(AccountService)
  webSocketService = inject(WebSocketService)
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
  symbolAliases:{ [key: string]: string } = {
    'INFY': 'Infosys','INFY:NSE': 'Infosys','7203': 'Toyota','002594': 'BYD','005930': 'Samsung',
    'D05': 'DBS Group','D05:SGX': 'DBS Group','QQQ': 'Invesco','2603:TWSE': 'Evergreen',
    '2603': 'Evergreen','AAPL':'Apple'
  };


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

    
        const queryParams = this.activatedRoute.snapshot.queryParams;
        
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

        //for websocket using front-end. not good practice
        // this.key = this.accountSvc.key
        // if (this.key) {
        //   console.info('hello, there is a key' + this.key)
        //   this.connectWebSocket(this.key);
        // }
      this.getWebSocketData()


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

      this.symbols$.then((symbol: string[]) => {
        console.info('Symbols:', symbol);
        this.watchList$ = this.stockSvc.getStocklistData(symbol);
      }).catch((error) => {
        console.error(error);
      });


}

getWebSocketData(): void {
  this.initialiseWebSocketStocks(this.symbolAliases);
  let stompClient = this.webSocketService.connect();

  stompClient.connect({}, () => {
    stompClient.send('/app/notify', {}, '');
    stompClient.subscribe('/topic/notification', (notifications: any) => {
      const data: string[] = JSON.parse(notifications.body);
      data.forEach((stockData: string) => {
        const stock: WebSocketStock = JSON.parse(stockData);

        const index = this.webSocketStocks.findIndex(s => s.symbol === stock.symbol || s.symbol === this.symbolAliases[stock.symbol]);

        if (index !== -1) {
          const existingStock = this.webSocketStocks[index];
          existingStock.exchange = stock.exchange;
          existingStock.currency = stock.currency;
          existingStock.volumeChanged = existingStock.volume !== stock.volume;
          existingStock.priceChanged = existingStock.price !== stock.price;
          existingStock.askChanged = existingStock.ask !== stock.ask;
          existingStock.bidChanged = existingStock.bid !== stock.bid;
          existingStock.previousVolume = existingStock.volume; 
          existingStock.previousPrice = existingStock.price;
          existingStock.previousAsk = existingStock.ask;
          existingStock.previousBid = existingStock.bid;
          existingStock.price = stock.price;
          existingStock.ask = stock.ask;
          existingStock.bid = stock.bid;
          existingStock.volume = stock.volume;
        } else {
          const newStock: WebSocketStock = {
            symbol: stock.symbol,
            exchange: stock.exchange,
            currency: stock.currency,
            price: stock.price,
            ask: stock.ask,
            bid: stock.bid,
            volume: stock.volume,
            volumeChanged: false,
            priceChanged: false,
            askChanged: false,
            bidChanged: false,
            previousVolume: 0,
            previousPrice: 0,
            previousAsk: 0,
            previousBid: 0,
          };
          this.webSocketStocks.push(newStock);
        }
      });
    });
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


  ngAfterViewInit():void{

  }

  ngOnChanges(): void{

  }


  viewStock(symbol:string){

    console.info('Printed the symbol:'+ symbol)
    this.stockSvc.symbol = symbol
    // this.router.navigate(['research']);

    const symbolPromise = new Promise((resolve) => {
      resolve(this.stockSvc.symbol);
    });

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
