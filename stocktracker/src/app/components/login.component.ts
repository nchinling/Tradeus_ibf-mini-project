import { Component, OnInit, inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AccountService } from '../account.service';
import { Observable, firstValueFrom } from 'rxjs';
import { LoginResponse } from '../models';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  login$!: Promise<LoginResponse>
  loginForm!: FormGroup

  fb = inject(FormBuilder)
  router = inject(Router)
  accountSvc = inject(AccountService)

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      // username: this.fb.control<string>('', [ Validators.required, Validators.minLength(5) ]),
      username: this.fb.control<string>('', [Validators.required, Validators.pattern('[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}')]),
      password: this.fb.control<string>('', [ Validators.required, Validators.minLength(5) ])
    })
  }


  invalidField(ctrlName:string): boolean{
    return !!(this.loginForm.get(ctrlName)?.invalid && this.loginForm.get(ctrlName)?.dirty)
    // return !!(this.loginForm.get(ctrlName)?.invalid)
  }


  login() {
    const username = this.loginForm.get('username')?.value
    const password = this.loginForm.get('password')?.value
    const parsedUsername = username.split('@')[0];

    //the username and password are passed to loginSvc for loginGuard
    this.accountSvc.username = username
    this.accountSvc.password = password
    console.info('username: ', username)
    console.info('password: ', password)

    //Using promise
    this.login$=firstValueFrom(this.accountSvc.login(username, password))
    this.login$.then((response) => {
      console.log('timestamp:', response.timestamp);
    });

    this.loginForm.reset
    // localStorage.setItem('loginForm', JSON.stringify(this.loginForm))
    this.router.navigate(['/dashboard', parsedUsername])
  }

}


