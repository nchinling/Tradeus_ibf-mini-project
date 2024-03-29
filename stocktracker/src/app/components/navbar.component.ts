import { Component, OnInit, inject } from '@angular/core';
import { NgbCollapse } from '@ng-bootstrap/ng-bootstrap';
import { AccountService } from '../account.service';
import { Observable, Subscription, of } from 'rxjs';
import { Router } from '@angular/router';
import { StockProfile } from '../models';
import { StockService } from '../stock.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'], 
  providers: [NgbCollapse]
})
export class NavbarComponent implements OnInit{
  isCollapsed = true;
  collapsed = true;
  toggleCollapsed(): void {
    this.collapsed = !this.collapsed;
  }
  
  isLoggedIn$!: Observable<boolean>

  //check if can be renamed
  private isLoggedInSubscription: Subscription | undefined;
  KEY = "username"
  parsedUsername = ''
  queryParams: any

  
  router = inject(Router)

  accountSvc = inject(AccountService)
  stockSvc = inject(StockService)



  ngOnInit(): void {
    this.isLoggedInSubscription = this.accountSvc.isLoggedInChanged.subscribe(isLoggedIn => {
      this.isLoggedIn$ = of(isLoggedIn);
      console.info('User is logged in: ' + isLoggedIn);
    });
    
    this.parsedUsername = this.accountSvc.parsedUsername
    this.queryParams = this.accountSvc.queryParams
    console.info('the parsedUsername is' + this.parsedUsername)
    console.info('the queryParams is' + this.queryParams)


  }

  logout(): void {
    // Clear the stored credentials 
    localStorage.removeItem(this.KEY);
    this.isLoggedIn$ = of(false);
    
    // reset 
    this.accountSvc.username=''
    this.accountSvc.password=''
    this.stockSvc.symbol=''
    this.accountSvc.key=''
    this.router.navigate(['/'])
  }

  ngAfterViewInit(): void{
    this.isLoggedIn$ = this.accountSvc.isLoggedInChanged
    this.parsedUsername = this.accountSvc.parsedUsername
    this.queryParams = this.accountSvc.queryParams
    console.info('the parsedUsername is' + this.parsedUsername)
    console.info('the queryParams is' + this.queryParams)

  }

  ngOnChanges(): void{
    this.isLoggedIn$ = this.accountSvc.isLoggedInChanged
    this.parsedUsername = this.accountSvc.parsedUsername
    this.queryParams = this.accountSvc.queryParams
    console.info('the parsedUsername is' + this.parsedUsername)
    console.info('the queryParams is' + this.queryParams)

  }

  ngOnDestroy(): void {
    if (this.isLoggedInSubscription) {
      this.isLoggedInSubscription.unsubscribe();
    }
  }

}
