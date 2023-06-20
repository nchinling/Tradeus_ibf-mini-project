import { Component, Injectable, Input, OnInit, Output, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { Observable, Subject, combineLatest, first, forkJoin, of } from 'rxjs';
import { AccountService } from '../account.service';
import { LoginResponse, RegisterResponse, Stock, Market, MarketIndex, StockInfo, StockProfile } from '../models';
import { StockService } from '../stock.service';
import { DashboardComponent } from './dashboard.component';


@Component({
  selector: 'app-research',
  templateUrl: './research.component.html',
  styleUrls: ['./research.component.css']
})
export class ResearchComponent  {
  stockSvc = inject(StockService)
  accountSvc = inject(AccountService)
  dashboardComp = inject(DashboardComponent)
  activatedRoute = inject(ActivatedRoute)
  title = inject(Title)
  fb = inject(FormBuilder)

  loginResponse$!: Observable<LoginResponse>
  registerResponse$!: Observable<RegisterResponse>
  errorMessage$!: Observable<string>
  username!: string
  status!: string
  timestamp!: string
  accountId!: string

  
  //stock variables
  stock$!: Promise<Stock>
  stockProfile$!: Promise<StockProfile>
  symbol$!: Observable<string>
  

  //for watchlist
  symbol!:string
  watchlist: string[] = []
  isFollowed!:boolean 
  isButtonClicked:boolean = false;



  markets: Market[] = [
    { symbol: "SPX", interval: "1day" },
    { symbol: "VIX", interval: "1day" },
    { symbol: "IXIC", interval: "1day" },
    { symbol: "DJI", interval: "1day" },
    { symbol: "RUT", interval: "1day" }
  ];

  marketIndex$!: Promise<MarketIndex[]>

  //for companies list
  @Input()
  limit = 10

  @Input()
  skip = 0

  @Input()
  filter = ""

  @Input()
  exchange = "nyse"

  stockInfoList$!: Promise<StockInfo[]>
 
  stockDataForm!: FormGroup
  loadStock: string = 'AAPL'
  loadInterval: string = '1min'

  //for chart
  initialChartSymbol!:string
  

 

  ngOnInit(): void {
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

      //load market data
      // this.marketIndex$ = this.stockSvc.getMarketData('SPX', '1day');
      this.marketIndex$ = this.stockSvc.getMarketData(this.markets);
      // this.stockList$ = this.stockSvc.getStockList()

      //load get stock data form
      this.stockDataForm = this.createStockDataForm()
      this.stockInfoList$ = this.stockSvc.getStocksList(this.exchange, this.filter, this.limit, this.skip)
      
      this.loadStock = this.stockSvc.symbol
      this.initialChartSymbol = 'AAPL'

      const symbolToLoad = (this.loadStock !== '') ? this.loadStock : 'AAPL';
      this.stock$ = this.stockSvc.getStockData(symbolToLoad, this.loadInterval);
      this.stockProfile$ = this.stockSvc.getStockProfile(symbolToLoad); 

    
  }

  ngAfterViewInit():void{
    // this.loginResponse$ = this.accountSvc.onLoginRequest
    // this.registerResponse$ = this.accountSvc.onRegisterRequest
  }



  getStockData(symbol?: string) {
    let interval = '5min'

    if (!symbol) {
      symbol = this.stockDataForm.get('symbol')?.value;
      interval = this.stockDataForm.get('interval')?.value
    }
  
    if (symbol && interval) {
      console.info('>> symbol: ', symbol);
      console.info('>> interval: ', interval);
      this.stock$ = this.stockSvc.getStockData(symbol, interval);
      this.stockProfile$ = this.stockSvc.getStockProfile(symbol); 
    }
  }

  private createStockDataForm(): FormGroup {
    return this.fb.group({
      symbol: this.fb.control<string>('', [ Validators.required ]),
      interval: this.fb.control<string>(this.loadInterval, [ Validators.required ])
    })
  }



  modifyWatchlist(symbol: string) {

    this.isButtonClicked = true
    this.watchlist = this.stockSvc.symbols
    this.symbol = symbol
  
    const index = this.watchlist.indexOf(symbol);
    this.isFollowed = this.watchlist.includes(symbol);

    setTimeout(() => {
      this.isButtonClicked = false;
    }, 3000); // Adjust the delay as per your requirements


    if (index !== -1) {
      // Symbol exists in the list, so remove it
      console.info("Already added to watchlist" + symbol)
    } else {
      // Symbol doesn't exist in the list, so add it
      console.info("Added to watchlist" + symbol)
    }
  
    console.info('added this symbol: ' + this.symbol)
    this.stockSvc.modifyWatchlist(this.symbol, this.username);
    console.info('I have sent this to dashboard: ' + symbol)
  }

  
  //for displaying stocks list
  fetchChanges(limit: string) {
    this.limit = +limit
    this.stockInfoList$= this.stockSvc.getStocksList(this.exchange, this.filter, this.limit, this.skip)
  }

  fetchExchange(exchange: string) {
    this.exchange = exchange
    this.stockInfoList$= this.stockSvc.getStocksList(this.exchange, this.filter, this.limit, this.skip)
  }

  filtering(text: string) {
    this.filter = text
    this.stockInfoList$= this.stockSvc.getStocksList(this.exchange, this.filter, this.limit, this.skip)
  }

  page(d: number) {
    if (d >= 0)
      this.skip += this.limit
    else
      this.skip = Math.max(0, this.skip - this.limit)

    this.stockInfoList$ = this.stockSvc.getStocksList(this.exchange, this.filter, this.limit, this.skip)
  }

  hasPreviousPage(): boolean {
    return this.skip >= this.limit;
  }
  
  // async hasNextPage(): Promise<boolean> {
  //   const stockInfoList = await this.stockInfoList$;
  //   return stockInfoList && stockInfoList.length === this.limit;
  // }

  // hasPreviousPage(): boolean {
  //   return this.skip - this.limit >= 0;
  // }
  
  // async hasNextPage(): Promise<boolean> {
  //   const stockInfoList = await this.stockInfoList$;
  //   return stockInfoList && stockInfoList.length === this.limit;
  // }




}

