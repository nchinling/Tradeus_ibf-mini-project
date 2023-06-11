import { Component, OnInit, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { AccountService } from '../account.service';
import { LoginResponse, RegisterResponse } from '../models';
import { RegisterComponent } from './register.component';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  accountSvc = inject(AccountService)


  loginResponse$!: Observable<LoginResponse>
  registerResponse$!: Observable<RegisterResponse>
  errorMessage$!: Observable<string>

  ngOnInit(): void {
    // this.loginResponse$ = this.accountSvc.onLoginRequest
    this.registerResponse$ = this.accountSvc.onRegisterRequest
    this.loginResponse$ = this.accountSvc.onLoginRequest
    this.errorMessage$ = this.accountSvc.onErrorMessage
 
  
  }

  ngAfterViewInit():void{
    this.loginResponse$ = this.accountSvc.onLoginRequest
    this.registerResponse$ = this.accountSvc.onRegisterRequest
 
  }

}
