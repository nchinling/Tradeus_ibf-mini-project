import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { environment } from 'src/environments/environment';
import { loadStripe } from '@stripe/stripe-js';
import { Router } from '@angular/router';
import { Observable, concatMap, first, map, tap } from 'rxjs';
import { WebSocketStock } from '../models';
import { WebSocketService } from '../websocket.service';
import { AccountService } from '../account.service';


@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent {
  // constructor(private http: HttpClient) {}
  router = inject(Router)
  http = inject(HttpClient)
  webSocketSvc = inject(WebSocketService)
  accountSvc = inject(AccountService)

  customAmount!:number
  username!: string
  isLoading = false;
  
  async pay(amount: number): Promise<void> {
    this.isLoading = true;
    const payment = {
      name: 'Buy Me a Coffee',
      currency: 'sgd',
      amount: amount,
      quantity: '1',
      
    };

    this.username = this.accountSvc.username

    setTimeout(() => {
      this.router.navigate(['/dashboard', this.username]);
      this.isLoading = false;
  
    }, 3000);

    this.http
    // .post(`${environment.serverUrl}/payment`, payment, { responseType: 'text' })
    .post(`/api/payment`, payment, { responseType: 'text' })
    .subscribe({
      next: (sessionId: string) => {
        console.log('Session ID:', sessionId);
  
        this.http.get(`/api/checkout/${sessionId}`, { responseType: 'text' }).subscribe({
          next: (checkoutUrl: string) => {
            console.log('I am here. the Checkout URL is:', checkoutUrl);
            window.open(checkoutUrl, '_blank', 'noopener');
          },
          error: (error: any) => {
            console.error(error);
          }
        });
      },
      error: (error: any) => {
        console.error(error);
      }
    });
  }

  
}
