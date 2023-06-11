import { Component, OnInit, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { AccountService } from '../account.service';
import { LoginResponse, RegisterResponse } from '../models';
import { RegisterComponent } from './register.component';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  accountSvc = inject(AccountService)
  activatedRoute = inject(ActivatedRoute)


  loginResponse$!: Observable<LoginResponse>
  registerResponse$!: Observable<RegisterResponse>
  errorMessage$!: Observable<string>
  username!: string
  status!: string
  timestamp!: string
  accountId!: string

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

  console.log('Status:', this.status);
  console.log('Timestamp:', this.timestamp);
  console.log('Account ID:', this.accountId);
  console.log('Username:', this.username);
  }

  ngAfterViewInit():void{
    this.loginResponse$ = this.accountSvc.onLoginRequest
    this.registerResponse$ = this.accountSvc.onRegisterRequest
 
  }

}
