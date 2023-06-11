import { Injectable, OnInit, inject } from "@angular/core";
import { Observable, Subject, catchError, filter, of, tap, throwError } from "rxjs";
import { ErrorResponse, LoginResponse, RegisterData, RegisterResponse } from "./models";
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from "@angular/common/http";
import { Router } from "@angular/router";
// import { v4 as uuidv4 } from 'uuid'. need to install package

const URL_API_TRADE_SERVER = 'http://localhost:8080/api'

@Injectable()
export class AccountService {

  onLoginRequest = new Subject<LoginResponse>()
  onRegisterRequest = new Subject<RegisterResponse>()
  onErrorResponse = new Subject<ErrorResponse>()
  onErrorMessage = new Subject<string>()

  http=inject(HttpClient)
  router = inject(Router)

  username = "";
  password = "";


  hasLogin(): boolean {
    return !!(this.username&&this.password)
   
  }


  registerAccount(data: RegisterData ): Observable<RegisterResponse> {
    // Content-Type: application/x-www-form-urlencoded
    // Accept: application/json

    const form = new HttpParams()
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


  //A) Works with both Observable and Promise
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
      //the fired onRequest.next is received in dashboard component's ngOnit 
      tap(response => this.onLoginRequest.next(response))
    );





  }






}
