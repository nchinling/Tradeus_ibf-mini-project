import { inject } from "@angular/core";
import { CanActivateFn, CanDeactivateFn, Router } from "@angular/router";
import { LoginService } from "./login.service";

export interface LeaveComponent {
  canExit(): boolean
  getMessage(): string
}


export const loginGuard: CanActivateFn = (route, state) => {

    const router = inject(Router)
    const loginSvc = inject(LoginService)

    if (loginSvc.hasLogin())
        return true
    console.info('>>>>>hasLogin is >>>>>' +loginSvc.hasLogin())
    return router.createUrlTree(['/'])
}

export const leaveComp: CanDeactivateFn<LeaveComponent> = (comp, currRoute, currState, nextState) => {
  if (comp.canExit())
    return true

  return confirm(comp.getMessage())
}
