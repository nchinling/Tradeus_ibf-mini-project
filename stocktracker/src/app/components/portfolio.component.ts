import { Component, Input, inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable, Subject, filter, firstValueFrom, from, mergeMap, of, switchMap } from 'rxjs';
import { AccountService } from '../account.service';
import { AnnualisedPortfolioData, PortfolioData, RegisterResponse, Stock, StockInfo, TradeData, TradeResponse, UserData } from '../models';
import { StockService } from '../stock.service';

@Component({
  selector: 'app-portfolio',
  templateUrl: './portfolio.component.html',
  styleUrls: ['./portfolio.component.css']
})
export class PortfolioComponent {

  register$!: Promise<RegisterResponse>
  stockInfoList$!: Promise<StockInfo[]>
  portfolioSymbols$!:Promise<string[]>
  portfolioData$!:Promise<PortfolioData[]>
  annualisedPortfolioData$!:Observable<AnnualisedPortfolioData[]>

  tradeResponse$!: Promise<TradeResponse>
  stockSearch$!: Observable<StockInfo[]>
  stock$!: Promise<Stock>
  symbol!: string
  currency!: string

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

    this.annualisedPortfolioData$ = this.stockSvc.getAnnualisedPortfolioData(this.accountId);

    this.portfolioSymbols$ = this.stockSvc.getPortfolioSymbols(this.accountId)
    console.info('this.symbols$ is' + this.portfolioSymbols$)

    this.portfolioSymbols$.then((symbol: string[]) => {
      console.info('Symbols:', symbol);
      this.portfolioData$ = this.stockSvc.getPortfolioData(symbol, this.accountId);
    }).catch((error) => {
      console.error(error);
    });

    
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
      units: this.fb.control<number>(0, [ Validators.required, Validators.min(1)]),
      price: this.fb.control<number>(0, [ Validators.required, Validators.min(0.0001)]),
      fee: this.fb.control<number>(0, [ Validators.required, Validators.min(0)]),
      // date: this.fb.control('', [ Validators.required]),
      date: ['', [Validators.required, this.futureDateValidator]],
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

  private futureDateValidator(control: AbstractControl): ValidationErrors | null {
    const selectedDate = new Date(control.value);
    const currentDate = new Date();
  
    return selectedDate <= currentDate ? null : { futureDate: true };
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
    tradeData.currency = this.currency
  


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
      this.portfolioSymbols$ = this.stockSvc.getPortfolioSymbols(this.accountId)
      console.info('this.symbols$ is' + this.portfolioSymbols$)
      this.annualisedPortfolioData$ = this.stockSvc.getAnnualisedPortfolioData(this.accountId);

      this.portfolioSymbols$.then((symbol: string[]) => {
        console.info('Symbols:', symbol);
        this.portfolioData$ = this.stockSvc.getPortfolioData(symbol, this.accountId);
      }).catch((error) => {
        console.error(error);
      });

    }).catch((error)=>{
  
      this.errorMessage = error.error;
      console.info('this.errorMessage is ' + this.errorMessage)

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
      // this.stock$.then(stockData => {
      //   this.currency = stockData.currency;
        
      // });
      this.stockSvc.getStockData(symbol, interval)
        .then(stockData => {
          // this.patchNameField(stockData.name); 
          this.currency = stockData.currency
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

  calculateTotalReturn(portfolioData: any[]): number {
    return portfolioData.reduce((total, data) => total + data.total_return, 0);
  }

  calculateTotalPercentageReturn(portfolioData: any[]): number {
    const totalReturn = this.calculateTotalReturn(portfolioData);
    const totalInvestment = portfolioData.reduce((total, data) => total + data.buy_total_price, 0);
    return (totalReturn / totalInvestment) * 100;
  }

  
  removeFromCumulativePortfolio(index: number) {
    const symbolToRemove: string = this.stockSvc.portfolioSymbols[index];
    console.info('To remove symbol: ' + symbolToRemove);
  
    this.stockSvc.removeFromPortfolio(index, this.accountId)
      .then(() => {
        console.info('Symbol removed successfully');
        return this.stockSvc.getPortfolioSymbols(this.accountId);
      })
      .then((symbol: string[]) => {
        console.info('The updated list of Symbols after removal are:', symbol);
        this.portfolioSymbols$ = Promise.resolve(symbol);
        this.annualisedPortfolioData$ = this.stockSvc.getAnnualisedPortfolioData(this.accountId);
        return this.stockSvc.getPortfolioData(symbol, this.accountId);
      })
      .then((allPortfolioData: PortfolioData[]) => {
        this.portfolioData$ = Promise.resolve(allPortfolioData);
      })
      .catch((error) => {
        console.error(error);
      });
      // this.annualisedPortfolioData$ = this.stockSvc.getAnnualisedPortfolioData(this.accountId);
  }


  
  removeFromAnnualisedPortfolio(symbol: string, date: Date) {
    
    console.info('To remove symbol: ' + symbol);
  
    this.stockSvc.removeFromAnnualisedPortfolio(symbol, date, this.accountId)
      .then(() => {
        console.info('Symbol removed successfully');
        return this.stockSvc.getPortfolioSymbols(this.accountId);
      })
      .then((symbol: string[]) => {
        console.info('The updated list of Symbols after removal are:', symbol);
        this.portfolioSymbols$ = Promise.resolve(symbol);
        this.annualisedPortfolioData$ = this.stockSvc.getAnnualisedPortfolioData(this.accountId);
        return this.stockSvc.getPortfolioData(symbol, this.accountId);
      })
      .then((allPortfolioData: PortfolioData[]) => {
        this.portfolioData$ = Promise.resolve(allPortfolioData);
      })
      .catch((error) => {
        console.error(error);
      });
      // this.annualisedPortfolioData$ = this.stockSvc.getAnnualisedPortfolioData(this.accountId);
  }





}
