import { Component, inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators, AbstractControl } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable, firstValueFrom } from 'rxjs';
import { AccountService } from '../account.service';
import { RegisterResponse, UserData } from '../models';


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {

  register$!: Promise<RegisterResponse>

  registerForm!: FormGroup
  errorMessage!: string;

  isLoading = false;


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
      name: this.fb.control('', [ Validators.required, Validators.minLength(5)]),
      username: this.fb.control('', [ Validators.required, Validators.pattern('[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}')]),
      date_of_birth: this.fb.control('', [Validators.required, this.minimumAgeValidator(18)]),
      password: this.fb.control('', [Validators.required, Validators.minLength(8), Validators.pattern('^(?=.*[a-zA-Z])(?=.*[@#$%^&+=]).*$')]),
      mobile_no: this.fb.control('', [ Validators.required, Validators.minLength(8)]),
      nationality: this.fb.control('', [ Validators.required]),
      address: this.fb.control('', [ Validators.required, Validators.minLength(8)]),
    })
  }

  private minimumAgeValidator(minAge: number) {
    return (control: AbstractControl) => {
      if (control.value) {
        const today = new Date();
        const birthDate = new Date(control.value);
        let age = today.getFullYear() - birthDate.getFullYear();
        const monthDiff = today.getMonth() - birthDate.getMonth();
        if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
          age--;
        }
        if (age < minAge) {
          return { minimumAge: true };
        }
      }
      return null;
    };
  }

  canExit(): boolean {
   
    return !this.registerForm.dirty
  }


  invalidField(ctrlName:string): boolean{
    return !!(this.registerForm.get(ctrlName)?.invalid && 
          this.registerForm.get(ctrlName)?.dirty)
  }


  registerAccount() {
    this.isLoading = true;
    const registerData:UserData = this.registerForm.value
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

    setTimeout(() => {
      this.isLoading = false;
    }, 4000);

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

      this.accountSvc.account_id = response.account_id


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
