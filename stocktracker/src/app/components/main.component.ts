import { Component, inject } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';
import { WebSocketService } from '../websocket.service';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent {

  http=inject(HttpClient)

  public notifications$: Observable<number>;
  private notificationsSubject: BehaviorSubject<number>;
  
constructor(private webSocketService: WebSocketService) {
  this.notificationsSubject = new BehaviorSubject<number>(0);
  this.notifications$ = this.notificationsSubject.asObservable();

  // this.getNotification();

  let stompClient = this.webSocketService.connect();

  stompClient.connect({}, (frame: any) => {
    this.getNotification();
    stompClient.subscribe('/topic/notification', (notifications: any) => {
      this.notificationsSubject.next(JSON.parse(notifications.body).count);
    });
  });
}


getNotification(): void {
  this.http.get('http://localhost:8080/notify', {responseType: 'text'}).subscribe({
    next: (response) => {
      console.log('Notification:', response);
    },
    error: (error) => {
      if (error instanceof HttpErrorResponse) {
        console.error('Failed to retrieve counter', error.status, error.statusText);
      } else {
        console.error('An unexpected error occurred:', error);
      }
    }
  });
}
}
