import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { Subject, lastValueFrom, tap, map, firstValueFrom, debounceTime, Observable } from "rxjs";
import { AnnualisedPortfolioData, Market, MarketIndex, PortfolioData, Stock, StockInfo, StockProfile } from "./models";

// const URL_API_TRADE_SERVER = 'http://localhost:8080/api'
const URL_API_TRADE_SERVER = '/api'

@Injectable()
export class StockService {

    http = inject(HttpClient)

    onStockRequest = new Subject<Stock>()
    onStockProfileRequest = new Subject<StockProfile>()
    onPortfolioDataRequest = new Subject<PortfolioData>()
    onAnnualisedPortfolioDataRequest = new Subject<AnnualisedPortfolioData[]>()
    onMarketRequest = new Subject<MarketIndex>()

    onStockSelection = new Subject<string>();
    symbols:string[]=[]
    portfolioSymbols: string[] = []
    symbol:string = ''
    portfolioSymbol: string=''

    getStockData(symbol:string, interval:string): Promise<Stock> {

        const queryParams = new HttpParams()
            .set('symbol', symbol)
            .set('interval', interval)
        console.info('>>>>>>sending to Stock server...')
        return lastValueFrom(
          this.http.get<Stock>(`${URL_API_TRADE_SERVER}/quote/stock`, { params: queryParams })
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

    
    getStockProfile(symbol:string): Promise<StockProfile> {

      console.info('>>>>>>sending to Stock server...')

      return lastValueFrom(
        this.http.get<StockProfile>(`${URL_API_TRADE_SERVER}/stock/profile/${symbol}`)
          .pipe(
            tap(resp => this.onStockProfileRequest.next(resp)),
            map(resp => ({ symbol: resp.symbol, name: resp.name, 
                        sector: resp.sector, industry: resp.industry,
                        ceo:resp.ceo, employees:resp.employees, website:resp.website,
                        description:resp.description, logoUrl: resp.logoUrl
                        }))
          )
      )
  } 

  
    getMarketData(markets: Market[]): Promise<MarketIndex[]> {
        const marketRequests: Promise<MarketIndex>[] = [];
      
        for (const market of markets) {
          const queryParams = new HttpParams()
            .set('symbol', market.symbol)
            .set('interval', market.interval);
      
          const request=lastValueFrom(this.http.get<MarketIndex>(`${URL_API_TRADE_SERVER}/quote/market`, { params: queryParams })
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
        return Promise.all(marketRequests);

      }

      getStocksList(exchange = "NYSE", filter = "", limit = 10, skip = 0): Promise<StockInfo[]> {
        const params = new HttpParams()
            .set("exchange", exchange)
            .set("filter", filter)
            .set("limit", limit)
            .set("skip", skip)
        return firstValueFrom(
          this.http.get<StockInfo[]>(`${URL_API_TRADE_SERVER}/stocklist`, { params })
          .pipe(debounceTime(1000)) 
        )
      }

      modifyWatchlist(symbol:string, username: string){
        //prevent duplicates
        if (!this.symbols.includes(symbol)) {
          this.symbols.push(symbol);
        }
        console.log(this.symbols)
        console.info('>>>>>>sending watchlist to Stock server...');

        const payload = {
          symbols: this.symbols,
          username: username
        };

        const headers = new HttpHeaders().set('Content-Type', 'application/json');

        return firstValueFrom(
          this.http.post<string[]>(`${URL_API_TRADE_SERVER}/watchlist`, payload, { headers })
        );
      
      }

      async getWatchlist(username: string): Promise<string[]> {
        return firstValueFrom(
          this.http.get<any[]>(`${URL_API_TRADE_SERVER}/watchlist?username=${username}`)
        ).then((response: any[]) => {
          this.symbols = response.map((item) => item.symbol);
          console.info('the symbols returned from getWatchlist are'+this.symbols)
          return this.symbols;
        });
      }

      getStocklistData(watchlist: string[]): Promise<Stock[]> {
        const watchlistRequests: Promise<Stock>[] = [];
        const interval = '5min'
        console.info('array passed to getStocklistData is ' + watchlist)

        for (const symbol of watchlist) {
          const queryParams = new HttpParams()
            .set('symbol', symbol)
            .set('interval', interval)
            console.info('the symbol in getWatchlistData is ' + symbol)
      
            const request =  lastValueFrom(
              this.http.get<Stock>(`${URL_API_TRADE_SERVER}/quote/watchlist`, { params: queryParams })
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
        
      
          watchlistRequests.push(request);
        }

        console.info('>>>>>>sending to Stock server...');
        // return Promise.all(marketRequests);
        return Promise.all(watchlistRequests);

      }
    

  removeFromWatchlist(index: number, username: string){
      
      this.symbols.splice(index, 1)
      console.log('the symbols after removal are' + this.symbols)
      console.info('sending watchlist to Stock server with' + this.symbols);
      const payload = {
        symbols: this.symbols,
        username: username
      };

      const headers = new HttpHeaders().set('Content-Type', 'application/json');

      return firstValueFrom(
        this.http.post<string[]>(`${URL_API_TRADE_SERVER}/watchlist`, payload, { headers })
      );
    

  }

  async getPortfolioSymbols(accountId: string): Promise<string[]> {
    return firstValueFrom(
      this.http.get<any[]>(`${URL_API_TRADE_SERVER}/portfolio?accountId=${accountId}`)
    ).then((response: any[]) => {
      this.portfolioSymbols = response.map((item) => item.symbol);
      console.info('the symbols returned from getPortfolioSymbols are'+this.portfolioSymbols)
      return this.portfolioSymbols;
    });
  }


  getPortfolioData(portfolioSymbols: string[], account_id: string): Promise<PortfolioData[]> {
    const portfolioListRequests: Promise<PortfolioData>[] = [];
    const interval = '5min'
    console.info('array passed to getPortfolioData is ' + portfolioSymbols)

    for (const symbol of portfolioSymbols) {
      const queryParams = new HttpParams()
        .set('symbol', symbol)
        .set('interval', interval)
        .set('account_id', account_id)
   
  
        const request =  lastValueFrom(
          this.http.get<PortfolioData>(`${URL_API_TRADE_SERVER}/quote/portfolio`, { params: queryParams })
            .pipe(
              tap(resp => this.onPortfolioDataRequest.next(resp)),
              map(resp => ({ account_id: resp.account_id, symbol: resp.symbol, stock_name: resp.stock_name, 
                          exchange: resp.exchange, currency: resp.currency,
                          units:resp.units, buy_unit_price:resp.buy_unit_price, buy_total_price:resp.buy_total_price,
                          unit_current_price:resp.unit_current_price, total_current_price:resp.total_current_price, 
                          total_return: resp.total_return, total_percentage_change:resp.total_percentage_change, 
                          datetime:resp.datetime
                          }))
            )
        )

      portfolioListRequests.push(request);
    }

    console.info('>>>>>>sending to Stock server...');
    return Promise.all(portfolioListRequests);

    }


  getAnnualisedPortfolioData(account_id: string): Observable<AnnualisedPortfolioData[]> {
    const interval = '5min';
  
    const queryParams = new HttpParams()
      .set('interval', interval)
      .set('account_id', account_id);
  
    return this.http.get<AnnualisedPortfolioData[]>(`${URL_API_TRADE_SERVER}/quote/portfolio/annualised`, { params: queryParams })
      .pipe(
        tap(resp => this.onAnnualisedPortfolioDataRequest.next(resp)),
        map(portfolioList => portfolioList.map(portfolioItem => ({
          account_id: portfolioItem.account_id, symbol: portfolioItem.symbol, stock_name: portfolioItem.stock_name,
          exchange: portfolioItem.exchange, currency: portfolioItem.currency, units: portfolioItem.units,
          buy_unit_price: portfolioItem.buy_unit_price, buy_total_price: portfolioItem.buy_total_price,
          unit_current_price: portfolioItem.unit_current_price, total_current_price: portfolioItem.total_current_price,
          total_return: portfolioItem.total_return, datetime: portfolioItem.datetime, annualised_profit: portfolioItem.annualised_profit
        })))
      );
  }
  

    removeFromPortfolio(index: number, accountId: string){
      
      
      this.portfolioSymbol = this.portfolioSymbols[index]
     
      console.info('sending symbol to Stock server with ' + this.portfolioSymbol);

      const headers = new HttpHeaders().set('Content-Type', 'application/json');

      return firstValueFrom(
        this.http.delete<string[]>(`${URL_API_TRADE_SERVER}/portfolioList?symbol=${this.portfolioSymbol}&accountId=${accountId}`, { headers })
      );
  }


  removeFromAnnualisedPortfolio(symbol: string, date: Date, accountId: string){
      
    console.info('sending symbol to Stock server with ' + this.portfolioSymbol);

    const headers = new HttpHeaders().set('Content-Type', 'application/json');

    return firstValueFrom(
      this.http.delete<string[]>(`${URL_API_TRADE_SERVER}/tradesList?symbol=${symbol}&date=${date}&accountId=${accountId}`, { headers })
    );

  }


}

