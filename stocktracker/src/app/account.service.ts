import { Injectable, OnInit, inject } from "@angular/core";
import { Observable, Subject, catchError, filter, lastValueFrom, map, of, tap, throwError } from "rxjs";
import { ErrorResponse, LoginResponse, UserData, RegisterResponse, Stock, TradeData, TradeResponse } from "./models";
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from "@angular/common/http";
import { Router } from "@angular/router";
// import { v4 as uuidv4 } from 'uuid'. need to install package

// const URL_API_TRADE_SERVER = 'http://localhost:8080/api'
const URL_API_TRADE_SERVER = '/api'

@Injectable()
export class AccountService {

  onLoginRequest = new Subject<LoginResponse>()
  onRegisterRequest = new Subject<RegisterResponse>()
  onSavePortfolioRequest = new Subject<TradeResponse>()
  onUserDataRequest = new Subject<UserData>()
  onErrorResponse = new Subject<ErrorResponse>()
  onErrorMessage = new Subject<string>()
  isLoggedInChanged = new Subject<boolean>()

  http=inject(HttpClient)
  router = inject(Router)

  username = "";
  password = "";
  parsedUsername = "";
  queryParams: any;
  account_id = ""
  KEY = "username"
  key!: string


  hasLogin(): boolean {
    if(this.username&&this.password)
      localStorage.setItem(this.KEY, this.username)
      const isLoggedIn = !!(this.username && this.password);
      this.isLoggedInChanged.next(isLoggedIn);
    return isLoggedIn;
 
  }


  logout(): void {
    // Clear the stored credentials from local storage
    localStorage.removeItem(this.KEY);
  }

  isAuthenticated(): boolean {
    // Check if there are stored credentials in local storage
    return localStorage.getItem(this.KEY) !== null;
  }



  registerAccount(data: UserData ): Observable<RegisterResponse> {
    // Content-Type: application/x-www-form-urlencoded
    // Accept: application/json

    console.info('I am passing to updateAccount name:' + data.username)
    console.info('I am passing to updateAccount name:' + data.password)

    const form = new HttpParams()
      .set("account_id", this.account_id? this.account_id : "")
      .set("name", data.name)
      .set("username", data.username)
      .set("password", data.password)
      .set("mobile_no", data.mobile_no)
      .set("nationality", data.nationality)
      .set("address", data.address)
      .set("date_of_birth", data.date_of_birth.toString())

    const headers = new HttpHeaders()
      .set("Content-Type", "application/x-www-form-urlencoded")

    return this.http.post<RegisterResponse>(`${URL_API_TRADE_SERVER}/register`, form.toString(), {headers}).pipe(
      catchError(error => {
        let errorMessage = 'An error occurred during registration: ' + error.message;
        console.error(errorMessage);
        
        if (error instanceof HttpErrorResponse && error.status === 500) {
          const serverError = error.error.error; 
          errorMessage = '>>>Server error: ' + serverError;
        }
        
        this.onErrorMessage.next(errorMessage);
        return throwError(() => ({ error: errorMessage }));
      }),

      filter((response) => response !== null), // Filter out null responses
      //the fired onRequest.next is received in dashboard component's ngOnit 
      tap(response => this.onRegisterRequest.next(response))
    );
    
  }


  
  updateAccount(data: UserData ): Observable<RegisterResponse> {
    // Content-Type: application/x-www-form-urlencoded
    // Accept: application/json

    console.info('I am passing to updateAccount accountId:' + this.account_id)
    console.info('username in updateAccount:' + this.username)
    console.info('I am passing to updateAccount name:' + data.password)

    const form = new HttpParams()
      .set("account_id", this.account_id)
      .set("name", data.name)
      .set("username", this.username)
      .set("password", data.password)
      .set("mobile_no", data.mobile_no)
      .set("nationality", data.nationality)
      .set("address", data.address)
      .set("date_of_birth", data.date_of_birth.toString())

    const headers = new HttpHeaders()
      .set("Content-Type", "application/x-www-form-urlencoded")

    return this.http.put<RegisterResponse>(`${URL_API_TRADE_SERVER}/update`, form.toString(), {headers}).pipe(
      catchError(error => {
        let errorMessage = 'An error occurred during update: ' + error.message;
        console.error(errorMessage);
        
        if (error instanceof HttpErrorResponse && error.status === 500) {
          const serverError = error.error.error; 
          errorMessage = '>>>Server error: ' + serverError;
        }
        
        this.onErrorMessage.next(errorMessage);
        return throwError(() => ({ error: errorMessage }));
      }),

      filter((response) => response !== null), // Filter out null responses
      //the fired onRequest.next is received in dashboard component's ngOnit 
      tap(response => this.onRegisterRequest.next(response))
    );
    
  }

  
  getUserData(username:string): Promise<UserData> {

    const queryParams = new HttpParams()
        .set('username', username)

    console.info('>>>>>>getting User data from server...')
    return lastValueFrom(
      this.http.get<UserData>(`${URL_API_TRADE_SERVER}/getuser`, { params: queryParams })
        .pipe(
          tap(resp => this.onUserDataRequest.next(resp)),
          map(resp => ({ account_id: resp.name, name: resp.name, password: resp.password, username: resp.username, 
                        address:resp.address,mobile_no: resp.mobile_no,
                        nationality:resp.nationality, date_of_birth:resp.date_of_birth
                      }))
        )
    )
}


  login(username: string, password: string): Observable<LoginResponse> {
    // Content-Type: application/x-www-form-urlencoded
    // Accept: application/json

    const form = new HttpParams()
      .set("username", username)
      .set("password", password)

    const headers = new HttpHeaders()
      .set("Content-Type", "application/x-www-form-urlencoded")

    return  this.http.post<LoginResponse>(`${URL_API_TRADE_SERVER}/login`, form.toString(), {headers}).pipe(
      catchError(error => {
        let errorMessage = 'An error occurred during login: ' + error.message;
        console.error(errorMessage);
        
        if (error instanceof HttpErrorResponse && error.status === 500) {
          const serverError = error.error.error; 
          errorMessage = '>>>Server error: ' + serverError;
        }
        
        this.onErrorMessage.next(errorMessage);
        return throwError(() => ({ error: errorMessage }));
      }),
      filter((response) => response !== null), // Filter out null responses
      //the fired onLoginRequest.next is received in dashboard component's ngOnit 
      tap(response => this.onLoginRequest.next(response))
    );
  }


  saveToPortfolio(data: TradeData ): Observable<TradeResponse> {
    // Content-Type: application/x-www-form-urlencoded
    // Accept: application/json

    console.info('I am passing to saveToPortfolio units:' + data.units)
    console.info('I am passing to saveToPortfolio price:' + data.price)

    const form = new HttpParams()
      .set("account_id", this.account_id)
      .set("username", this.username)
      .set("exchange", data.exchange)
      .set("symbol", data.symbol)
      .set("stockName", data.stockName)
      .set("units", data.units)
      .set("price", data.price)
      .set("currency", data.currency)
      .set("fee", data.fee)
      .set("date", data.date.toString())

    console.info('account_id in savePortfolio: ' + this.account_id)
    console.info('username in savePortfolio: ' + this.username)
    console.info('stockName in savePortfolio: ' + data.stockName)
    console.info('currency in savePortfolio: ' + data.currency)

    const headers = new HttpHeaders()
      .set("Content-Type", "application/x-www-form-urlencoded")

    return this.http.post<TradeResponse>(`${URL_API_TRADE_SERVER}/savetoportfolio`, form.toString(), {headers}).pipe(
      catchError(error => {
        let errorMessage = 'An error occurred while adding to portfolio: ' + error.message;
        console.error(errorMessage);
        
        if (error instanceof HttpErrorResponse && error.status === 500) {
          const serverError = error.error.error; 
          errorMessage = '>>>Server error: ' + serverError;
        }
        
        this.onErrorMessage.next(errorMessage);
        return throwError(() => ({ error: errorMessage }));
      }),

      filter((response) => response !== null), // Filter out null responses
      //the fired onRequest.next is received in dashboard component's ngOnit 
      tap(response => this.onSavePortfolioRequest.next(response))
    );
    
  }






}
