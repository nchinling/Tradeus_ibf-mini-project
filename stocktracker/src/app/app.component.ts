import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { AccountService } from './account.service';



@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'stocktracker';
  KEY ="username"

  router = inject(Router)
  accountSvc = inject(AccountService)
  
  ngOnInit() {
    localStorage.removeItem(this.KEY);
    this.accountSvc.username=''
    this.accountSvc.password=''
    this.router.navigate([''])
  }
}
