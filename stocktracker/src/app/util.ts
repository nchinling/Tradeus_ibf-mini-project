import { Injectable, inject } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, CanActivateChild, CanActivateFn, CanDeactivateFn, Router, RouterStateSnapshot } from "@angular/router";
import { AccountService } from "./account.service";

export interface LeaveComponent {
  canExit(): boolean
  getMessage(): string
}


export const loginGuard: CanActivateFn = (route, state) => {

    const router = inject(Router)
    const accountSvc = inject(AccountService)

    if (accountSvc.hasLogin() || accountSvc.isAuthenticated())
        return true
    console.info('>>>>>hasLogin is >>>>>' +accountSvc.hasLogin())
    console.info('>>>>>isAuthenticated is >>>>>' +accountSvc.isAuthenticated())
    return router.createUrlTree(['/'])
}

export const leaveComp: CanDeactivateFn<LeaveComponent> = (comp, currRoute, currState, nextState) => {
  if (comp.canExit())
    return true

  return confirm(comp.getMessage())
}


