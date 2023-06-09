import { Injectable, OnInit, inject } from "@angular/core";
// import { v4 as uuidv4 } from 'uuid'. need to install package


@Injectable()
export class LoginService {

  username = "";
  password = "";


  hasLogin(): boolean {
    return !!(this.username&&this.password)
   
  }

}
