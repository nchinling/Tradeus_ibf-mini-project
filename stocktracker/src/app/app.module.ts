import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { LoginComponent } from './components/login.component';

import { leaveComp, loginGuard } from './util';
import { RouterModule, Routes } from '@angular/router';
import { ListComponent } from './components/list.component';
import { MainComponent } from './components/main.component';
import { OrderComponent } from './components/order.component';
import { DashboardComponent } from './components/dashboard.component';
import { HttpClientModule } from '@angular/common/http'
import { ReactiveFormsModule } from '@angular/forms';
import { AccountService } from './account.service';
import { NavbarComponent } from './components/navbar.component';
import { NgbCollapse, NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { RegisterComponent } from './components/register.component';

const appRoutes: Routes = [
  { path: '', component: MainComponent, title: 'Main' },
  { path: 'login', component: LoginComponent, title: 'Log In' },
  { path: 'register', component: RegisterComponent, title: 'Register' },
  // { path: 'dashboard/:userId', component: DashboardComponent, canActivate: [ loginGuard ] },
  // { path: 'dashboard', component: DashboardComponent, canActivate: [ loginGuard ] },
  { path: 'dashboard/:parsedUsername', component: DashboardComponent, title: 'DashBoard', canActivate: [ loginGuard ]},
  { path: 'list', component: ListComponent, title: 'Stock List', canActivate: [ loginGuard ] },
  { path: 'order', component: OrderComponent, title: 'Order'
      , canActivate: [ loginGuard ], canDeactivate: [ leaveComp ] },
  { path: '**', redirectTo: '/', pathMatch: 'full' }
]


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    MainComponent,
    ListComponent,
    DashboardComponent, 
    OrderComponent, NavbarComponent, RegisterComponent,
  ],
  imports: [
    BrowserModule, ReactiveFormsModule, HttpClientModule,
    // bindToComponentInputs - V16
    // RouterModule.forRoot(appRoutes, { useHash: true, bindToComponentInputs: true })
    RouterModule.forRoot(appRoutes, { useHash: true}),
    NgbModule
  ],
  providers: [AccountService],
  bootstrap: [AppComponent]
})
export class AppModule { }
