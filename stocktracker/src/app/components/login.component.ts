import { Component, OnInit, inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginService } from '../login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  
  form!: FormGroup

  fb = inject(FormBuilder)
  router = inject(Router)
  loginSvc = inject(LoginService)

  ngOnInit(): void {
    this.form = this.fb.group({
      username: this.fb.control<string>('', [ Validators.required, Validators.minLength(5) ]),
      password: this.fb.control<string>('', [ Validators.required, Validators.minLength(5) ])
    })
  }


  login() {
    const username = this.form.get('username')?.value
    const password = this.form.get('password')?.value
    const parsedUsername = username.split('@')[0];

    //the username and password are passed to loginSvc for loginGuard
    this.loginSvc.username = username
    this.loginSvc.password = password
    console.info('username: ', username)
    console.info('password: ', password)
    this.router.navigate(['/dashboard', parsedUsername])
  }

}


