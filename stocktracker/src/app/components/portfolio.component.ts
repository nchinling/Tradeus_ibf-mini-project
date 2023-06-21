import { Component, Input, inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable, Subject, filter, firstValueFrom, from, mergeMap, of, switchMap } from 'rxjs';
import { AccountService } from '../account.service';
import { RegisterResponse, Stock, StockInfo, TradeData, TradeResponse, UserData } from '../models';
import { StockService } from '../stock.service';

@Component({
  selector: 'app-portfolio',
  templateUrl: './portfolio.component.html',
  styleUrls: ['./portfolio.component.css']
})
export class PortfolioComponent {

  register$!: Promise<RegisterResponse>
  stockInfoList$!: Promise<StockInfo[]>
  tradeResponse$!: Promise<TradeResponse>
  stockSearch$!: Observable<StockInfo[]>
  stock$!: Promise<Stock>
  symbol!: string

  portfolioForm!: FormGroup
  errorMessage!: string;
  username!: string
  parsedUsername!: string
  accountId!: string
  filter!:string
  portfolioList!: string[]

  searchInput = new Subject<string>
  // search$! : Promise<StockInfo[]>



  fb = inject(FormBuilder)
  router = inject(Router)
  accountSvc = inject(AccountService)
  stockSvc = inject(StockService)
  errorMessage$!: Observable<string>

  @Input()
  exchange = "nyse"

  ngOnInit(): void {
    this.accountId = this.accountSvc.account_id
    this.username = this.accountSvc.username
    this.portfolioForm = this.createForm()
    this.errorMessage$ = this.accountSvc.onErrorMessage;

    // this.stockInfoList$ = this.searchInput.pipe(
    //   this.stockSvc.getStocksList(this.exchange, title, 5, 0))

    this.stockSearch$ = this.searchInput.pipe(
      switchMap((text: string) => {
        return from(this.stockSvc.getStocksList(this.exchange, text, 5, 0));
      })
    );
    
  }

  ngAfterViewInit():void{
    this.errorMessage$ = this.accountSvc.onErrorMessage;
  }


  private createForm(): FormGroup {
    return this.fb.group({
      exchange: this.fb.control<string>('nyse', [ Validators.required]),
      stockName: this.fb.control<string>('', [ Validators.required]),
      units: this.fb.control<number>(2000, [ Validators.required]),
      price: this.fb.control<number>(1.60, [ Validators.required]),
      fee: this.fb.control<number>(20.45, [ Validators.required]),
      date: this.fb.control('', [ Validators.required]),
    })
  }

  canExit(): boolean {
    //return true if it's clean form
    return !this.portfolioForm.dirty
  }


  invalidField(ctrlName:string): boolean{
    return !!(this.portfolioForm.get(ctrlName)?.invalid && 
          this.portfolioForm.get(ctrlName)?.dirty)
  }


  processPortfolio() {
    const tradeData:TradeData = this.portfolioForm.value
    const exchange = this.portfolioForm.get('exchange')?.value
    const stockNameWithSymbol = this.portfolioForm.get('stockname')?.value
    const units = this.portfolioForm.get('units')?.value
    const price = this.portfolioForm.get('price')?.value
    const fee = this.portfolioForm.get('fee')?.value
    const date = this.portfolioForm.get('date')?.value
    tradeData.symbol = this.symbol
  


    console.info('trade data: ', tradeData)
    this.portfolioForm = this.createForm()

    //Using promise
    this.tradeResponse$=firstValueFrom(this.accountSvc.saveToPortfolio(tradeData))
    this.tradeResponse$.then((response) => {
      console.log('exchange:', response.exchange);
      console.log('stockName:', response.stockName);
      console.log('symbol:', response.symbol);
      console.log('units:', response.units);
      console.log('price:', response.price);
      console.log('units:', response.fee);
      console.log('price:', response.date);


      //refer to researchComp
      this.portfolioList = this.stockSvc.portfolioSymbols


    }).catch((error)=>{
  
      this.errorMessage = error.error;
      console.info('this.errorMessage is ' + this.errorMessage)
      // this.errorMessage$ = this.accountSvc.onErrorMessage;
      // this.portfolioForm.reset();
    });

  }

  filtering(text:string){
    this.searchInput.next(text as string)

    // this.stockInfoList$= this.stockSvc.getStocksList(this.exchange, this.filter, 5, 0)
  }


  fetchExchange(exchange: string) {
    this.exchange = exchange
    this.stockInfoList$= this.stockSvc.getStocksList(this.exchange, '', 5, 0)
  }

  getStockData(symbol: string) {
    let interval = '5min'
    this.symbol = symbol


    if (symbol && interval) {
      console.info('>> symbol: ', symbol);
      console.info('>> interval: ', interval);
      this.stock$ = this.stockSvc.getStockData(symbol, interval)
      this.stockSvc.getStockData(symbol, interval)
        .then(stockData => {
          // this.patchNameField(stockData.name); 
          this.patchNameField(`${stockData.name} (${stockData.symbol})`);
        });
    }
    // this.stockSearch$ = of([])
    this.stockSearch$ = this.searchInput.pipe(
      switchMap((text: string) => {
        return from(this.stockSvc.getStocksList(this.exchange, text, 5, 0));
      })
    );

  }


  private patchNameField(value: string) {
    this.portfolioForm.patchValue({ stockName:value });
  }


}
