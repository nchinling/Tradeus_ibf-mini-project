import { HttpClient } from '@angular/common/http';
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
      // cancelUrl: 'http://localhost:4200/#/cancel',
      // cancelUrl: `${window.location.origin}/cancel`,
      // successUrl: `${window.location.origin}/success`,

      // successUrl: 'http://localhost:8080/api/stripe-success',
      // cancelUrl: 'http://localhost:8080/api/stripe-cancelled',

      //for deployment when single server is used
      successUrl: `${window.location.origin}/api/stripe-success`,
      cancelUrl: `${window.location.origin}/api/stripe-cancelled`,
    
    };

    this.username = this.accountSvc.username

    setTimeout(() => {
      this.router.navigate(['/dashboard', this.username]);
      this.isLoading = false;
      // this.router.navigate(['/dashboard', this.username]);

    }, 3000);

    this.http
    .post(`${environment.serverUrl}/payment`, payment, { responseType: 'text' })
    .subscribe({
      next: (sessionId: string) => {
        console.log('Session ID:', sessionId);
        // Redirect to the server's endpoint for handling the Stripe checkout
        // window.location.href = `${environment.serverUrl}/checkout/${sessionId}`;
        // window.open(`${environment.serverUrl}/checkout/${sessionId}`, '_blank');

        const url = `${environment.serverUrl}/checkout/${sessionId}`;
        console.log(url);
        window.open(url, '_blank');


      },
      error: (error: any) => {
        // Handle any errors that occur during the payment request
        console.error(error);
      }
    });


  }




  
}
