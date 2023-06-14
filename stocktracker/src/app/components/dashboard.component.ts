import { Component, HostListener, OnInit, inject } from '@angular/core';
import { Observable, Subscription, interval } from 'rxjs';
import { AccountService } from '../account.service';
import { LoginResponse, MarketIndex, RegisterResponse, Stock, Market } from '../models';
import { RegisterComponent } from './register.component';
import { ActivatedRoute, NavigationStart, Router } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { StockService } from '../stock.service';


@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {


  stockSvc = inject(StockService)
  accountSvc = inject(AccountService)
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
  // marketIndex$!: Promise<MarketIndex>

  markets: Market[] = [
    { symbol: "SPX", interval: "1day" },
    { symbol: "VIX", interval: "1day" },
    { symbol: "IXIC", interval: "1day" },
    { symbol: "DJI", interval: "1day" },
    { symbol: "RUT", interval: "1day" }
  ];

  marketIndex$!: Promise<MarketIndex[]>
 

  stockDataForm!: FormGroup

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

      //load get stock data form
      this.stockDataForm = this.createStockDataForm()
    
  }

  ngAfterViewInit():void{
    this.loginResponse$ = this.accountSvc.onLoginRequest
    this.registerResponse$ = this.accountSvc.onRegisterRequest
  }


  getStockData() {
    //get form control field
    const symbol = this.stockDataForm.get('symbol')?.value
    const interval = this.stockDataForm.get('interval')?.value
    if (symbol && interval) {
      //get field value
      console.info('>> symbol: ', symbol);
      console.info('>> interval: ', interval);
      this.stock$ = this.stockSvc.getStockData(symbol, interval);
    }
  }

  private createStockDataForm(): FormGroup {
    return this.fb.group({
      symbol: this.fb.control<string>('AAPL', [ Validators.required ]),
      interval: this.fb.control<string>('1min', [ Validators.required ])
    })
  }

}
