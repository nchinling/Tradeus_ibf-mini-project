import { Component, inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators, AbstractControl } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable, firstValueFrom } from 'rxjs';
import { AccountService } from '../account.service';
import { RegisterResponse, UserData } from '../models';

@Component({
  selector: 'app-useredit',
  templateUrl: './useredit.component.html',
  styleUrls: ['./useredit.component.css']
})
export class UsereditComponent {

  
  updateResponse$!: Observable<RegisterResponse>
  userData$!: Promise<UserData>
  update$!: Promise<RegisterResponse>
  username!: string

  updateForm!: FormGroup
 
  errorMessage!: string;
  successMessage!: string;


  fb = inject(FormBuilder)
  router = inject(Router)
  accountSvc = inject(AccountService)
  errorMessage$!: Observable<string>

 ngOnInit(): void {
  
    this.updateResponse$ = this.accountSvc.onRegisterRequest

    //get user data 
    this.username = this.accountSvc.username
    this.userData$=this.accountSvc.getUserData(this.username)

    this.updateForm = this.createForm()
    this.errorMessage$ = this.accountSvc.onErrorMessage;
  }

  ngAfterViewInit():void{
    this.errorMessage$ = this.accountSvc.onErrorMessage;
    
  }

  private createForm(): FormGroup {
 
    this.userData$=this.accountSvc.getUserData(this.username)
    const formGroup = this.fb.group({
      name: ['', [ Validators.required, Validators.minLength(5)]],
      password: ['', [Validators.required, Validators.minLength(8), Validators.pattern('^(?=.*[a-zA-Z])(?=.*[@#$%^&+=]).*$')]],
      address: ['', [ Validators.required, Validators.minLength(8)]],
      mobile_no: ['', [ Validators.required, Validators.minLength(8)]],
      nationality: ['', Validators.required],
      date_of_birth: ['', [Validators.required, this.minimumAgeValidator(18)]],
    });


    this.userData$.then((userData) => {
      const date = new Date(userData.date_of_birth);
      const formattedDate = `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`;
    
      const userDataWithFormattedDate = {
        ...userData,
        date_of_birth: formattedDate,
      };
      formGroup.patchValue(userDataWithFormattedDate);
    });
    


    return formGroup;
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
    return !this.updateForm.dirty
  }


  invalidField(ctrlName:string): boolean{
    return !!(this.updateForm.get(ctrlName)?.invalid && 
          this.updateForm.get(ctrlName)?.dirty)
  }


 updateAccount() {
    const updatedUserData:UserData = this.updateForm.value
    console.info('>> data: ', updatedUserData)
    const name = this.updateForm.get('name')?.value
    const password = this.updateForm.get('password')?.value
    const mobile_no = this.updateForm.get('mobile_no')?.value
    const nationality = this.updateForm.get('nationality')?.value
    const date_of_birth = this.updateForm.get('date_of_birth')?.value
    const address = this.updateForm.get('address')?.value

 

    const parsedUsername = this.accountSvc.parsedUsername
    this.accountSvc.password = password
    console.info('data for update: ', updatedUserData)

    //Using promise
    this.update$=firstValueFrom(this.accountSvc.updateAccount(updatedUserData))
    this.update$.then((response) => {
      console.log('status:', response.status);

      this.updateForm.reset
      this.successMessage = 'Account has been successfully updated.'
      setTimeout(() => {
        this.router.navigate(['/dashboard', parsedUsername]);
      }, 2000); 

    }).catch((error)=>{
  
      this.errorMessage = error.error;
      console.info('this.errorMessage is ' + this.errorMessage)
    });

  }

}
