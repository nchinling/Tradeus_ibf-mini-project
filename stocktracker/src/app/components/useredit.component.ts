import { Component, inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
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
  // updateForm: FormGroup = new FormGroup({});

  errorMessage!: string;
  successMessage!: string;


  fb = inject(FormBuilder)
  router = inject(Router)
  accountSvc = inject(AccountService)
  errorMessage$!: Observable<string>

 ngOnInit(): void {
  // async ngOnInit(): Promise<void> {
    this.updateResponse$ = this.accountSvc.onRegisterRequest
    //get user data first 
    this.username = this.accountSvc.username
    this.userData$=this.accountSvc.getUserData(this.username)

    // const userData: UserData = this.accountSvc.getUserData(this.username);

    //
    this.updateForm = this.createForm()
    this.errorMessage$ = this.accountSvc.onErrorMessage;
  }

  ngAfterViewInit():void{
    this.errorMessage$ = this.accountSvc.onErrorMessage;
    
  }

  private createForm(): FormGroup {
    // const userData: UserData = this.accountSvc.getUserData(this.username);
    this.userData$=this.accountSvc.getUserData(this.username)
    const formGroup = this.fb.group({
      name: ['', Validators.required],
      password: ['', Validators.required],
      // username: ['', Validators.required],
      address: ['', Validators.required],
      mobile_no: ['', Validators.required],
      nationality: ['', Validators.required],
      date_of_birth: ['', Validators.required],
    });

    // this.userData$.then((userData) => {
    //   formGroup.patchValue(userData);
    // });

    // this.userData$.then((userData) => {
    //   const userDataWithFormattedDate = {
    //     ...userData,
    //     date_of_birth: userData.date_of_birth.toISOString().substring(0, 10), // Convert Date to string with format 'YYYY-MM-DD'
    //   };
    //   formGroup.patchValue(userDataWithFormattedDate);
    // });

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
  
  canExit(): boolean {
    //return true if it's clean form
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
    // const username = this.updateForm.get('username')?.value
    const password = this.updateForm.get('password')?.value
    const mobile_no = this.updateForm.get('mobile_no')?.value
    const nationality = this.updateForm.get('nationality')?.value
    const date_of_birth = this.updateForm.get('date_of_birth')?.value
    const address = this.updateForm.get('address')?.value

    // const parsedUsername = username.split('@')[0];

    //the username and password are passed to accountSvc for loginGuard
    // this.accountSvc.username = username
    this.accountSvc.password = password
    console.info('data for update: ', updatedUserData)

    //Using promise
    this.update$=firstValueFrom(this.accountSvc.updateAccount(updatedUserData))
    this.update$.then((response) => {
      console.log('status:', response.status);

      this.updateForm.reset
      this.successMessage = 'Account has been successfully updated.'

      // this.router.navigate(['/dashboard', parsedUsername], { queryParams: queryParams })
    }).catch((error)=>{
  
      this.errorMessage = error.error;
      console.info('this.errorMessage is ' + this.errorMessage)

    });


  }


}
