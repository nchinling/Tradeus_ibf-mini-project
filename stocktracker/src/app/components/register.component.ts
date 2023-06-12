import { Component, Injectable, inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable, Subject, firstValueFrom } from 'rxjs';
import { AccountService } from '../account.service';
import { LoginResponse, RegisterData, RegisterResponse } from '../models';


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {

  register$!: Promise<RegisterResponse>

  registerForm!: FormGroup
  errorMessage!: string;


  fb = inject(FormBuilder)
  router = inject(Router)
  accountSvc = inject(AccountService)
  errorMessage$!: Observable<string>

  ngOnInit(): void {
    this.registerForm = this.createForm()
    this.errorMessage$ = this.accountSvc.onErrorMessage;
  }

  ngAfterViewInit():void{
    this.errorMessage$ = this.accountSvc.onErrorMessage;
  }


  private createForm(): FormGroup {
    return this.fb.group({
      name: this.fb.control('Chin Ling', [ Validators.required]),
      username: this.fb.control('ncl@gmail.com', [ Validators.required]),
      date_of_birth: this.fb.control('', [ Validators.required]),
      password: this.fb.control('88888', [ Validators.required]),
      mobile_no: this.fb.control('94892015', [ Validators.required]),
      nationality: this.fb.control('Singaporean', [ Validators.required]),
      address: this.fb.control('86 Dawson Road', [ Validators.required]),
    })
  }

  canExit(): boolean {
    //return true if it's clean form
    return !this.registerForm.dirty
  }


  invalidField(ctrlName:string): boolean{
    return !!(this.registerForm.get(ctrlName)?.invalid && 
          this.registerForm.get(ctrlName)?.dirty)
  }


  registerAccount() {
    const registerData:RegisterData = this.registerForm.value
    console.info('>> data: ', registerData)
    const name = this.registerForm.get('name')?.value
    const username = this.registerForm.get('username')?.value
    const password = this.registerForm.get('password')?.value
    const mobile_no = this.registerForm.get('mobile_no')?.value
    const nationality = this.registerForm.get('nationality')?.value
    const date_of_birth = this.registerForm.get('date_of_birth')?.value
    const address = this.registerForm.get('address')?.value

    const parsedUsername = username.split('@')[0];

    //the username and password are passed to accountSvc for loginGuard
    this.accountSvc.username = username
    this.accountSvc.password = password
    console.info('registation data: ', registerData)

    //Using promise
    this.register$=firstValueFrom(this.accountSvc.registerAccount(registerData))
    this.register$.then((response) => {
      console.log('status:', response.status);
      console.log('timestamp:', response.timestamp);
      console.log('account_id:', response.account_id);
      console.log('response:', response);

      const queryParams = {
        // status: response.status,
        // timestamp: response.timestamp,
        account_id: response.account_id,
        username: response.username
      };

      // const dashboardUrl = `#/dashboard/${parsedUsername}?account_id=${response.account_id}&username=${response.username}`;

      // // Store the dashboard URL in localStorage
      // localStorage.setItem('dashboardUrl', dashboardUrl);

      this.registerForm.reset

      this.router.navigate(['/dashboard', parsedUsername], { queryParams: queryParams })
    }).catch((error)=>{
  
      this.errorMessage = error.error;
      console.info('this.errorMessage is ' + this.errorMessage)
      // this.errorMessage$ = this.accountSvc.onErrorMessage;
      // this.registerForm.reset();
    });


  }
}
