import { Injectable } from "@angular/core";
import * as SockJs from 'sockjs-client';
import * as Stomp from 'stompjs';


//working web socket
@Injectable()
export class WebSocketService {

    // Open connection with the back-end socket
    public connect() {
        // let socket = new SockJs(`http://localhost:8080/socket`);
        let socket = new SockJs(`api/socket`);

        let stompClient = Stomp.over(socket);

        return stompClient;
    }

    
}








