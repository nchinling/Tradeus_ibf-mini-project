import { NgModule, isDevMode } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { LoginComponent } from './components/login.component';

import { leaveComp, loginGuard} from './util';
import { RouterModule, Routes } from '@angular/router';
import { MainComponent } from './components/main.component';
import { DashboardComponent } from './components/dashboard.component';
import { HttpClientModule } from '@angular/common/http'
import { ReactiveFormsModule } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { AccountService } from './account.service';
import { NavbarComponent } from './components/navbar.component';
import { NgbCollapse, NgbModule, NgbNavModule } from '@ng-bootstrap/ng-bootstrap';
import { RegisterComponent } from './components/register.component';
import { LearnComponent } from './components/main/learn.component';
import { AboutComponent } from './components/main/about.component';
import { StockService } from './stock.service';
import { ResearchComponent } from './components/research.component';
import { PortfolioComponent } from './components/portfolio.component';
import { UsereditComponent } from './components/useredit.component';
import { ChartComponent } from './components/chart.component';
import { ChartService } from './chart.service';
import { WebSocketService } from './websocket.service';
import { CheckoutComponent } from './components/checkout.component';
import { ServiceWorkerModule } from '@angular/service-worker';
import { GettingStartedComponent } from './components/main/getting-started.component';
import { AiComponent } from './components/ai.component';


const appRoutes: Routes = [
  { path: '', component: MainComponent, title: 'Welcome to Tradeus' },
  { path: 'learn', component: LearnComponent, title: 'Learn' },
  { path: 'getting_started', component: GettingStartedComponent, title: 'Getting Started' },
  { path: 'about', component: AboutComponent, title: 'About Us' },
  { path: 'login', component: LoginComponent, title: 'Log In' },
  { path: 'register', component: RegisterComponent, title: 'Register' },
  { path: 'useredit', component: UsereditComponent, title: 'Edit Profile', canActivate: [ loginGuard] },
  { path: 'dashboard/:parsedUsername', component: DashboardComponent, title: 'DashBoard', canActivate: [loginGuard]},
  { path: 'research', component: ResearchComponent, title: 'Research', canActivate: [ loginGuard] },
  { path: 'ai', component: AiComponent, title: 'AISA', canActivate: [ loginGuard] },
  { path: 'portfolio/:parsedUsername', component: PortfolioComponent, title: 'Portfolio', canActivate: [ loginGuard] },
  { path: 'checkout', component: CheckoutComponent, title: 'Buy me a coffee'},
  { path: '**', redirectTo: '/', pathMatch: 'full' }
]


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    MainComponent,
    DashboardComponent, NavbarComponent, RegisterComponent, 
    LearnComponent, AboutComponent, ResearchComponent, 
    PortfolioComponent, UsereditComponent, ChartComponent, 
    CheckoutComponent, GettingStartedComponent, AiComponent
    
  ],
  imports: [
    BrowserModule, ReactiveFormsModule, FormsModule, HttpClientModule,
    // bindToComponentInputs - V16
    // RouterModule.forRoot(appRoutes, { useHash: true, bindToComponentInputs: true })
    RouterModule.forRoot(appRoutes, { useHash: true}),
    // RouterModule.forRoot(appRoutes),
   
    NgbNavModule, NgbModule, ServiceWorkerModule.register('ngsw-worker.js', {
  enabled: !isDevMode(),
  // Register the ServiceWorker as soon as the application is stable
  // or after 30 seconds (whichever comes first).
  registrationStrategy: 'registerWhenStable:30000'
})
    
  ],
  exports: [RouterModule],
  providers: [AccountService, StockService, ChartService, WebSocketService, DashboardComponent, ResearchComponent],
  bootstrap: [AppComponent]
})
export class AppModule { }
