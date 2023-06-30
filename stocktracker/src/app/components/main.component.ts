import { Component, inject } from '@angular/core';
import { Observable, BehaviorSubject, Subject, filter } from 'rxjs';
import { WebSocketService } from '../websocket.service';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { WebSocketStock } from '../models';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent {


ngOnInit():void{
  
}


}
