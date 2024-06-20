import { inject } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot } from "@angular/router"
import { Observable } from "rxjs"
import { JwtService } from "./services/jwt.service";


export const enterMain: CanActivateFn = 
    (route: ActivatedRouteSnapshot, state: RouterStateSnapshot)
        : boolean | Promise<boolean> | Observable<boolean> => {

    const router = inject(Router)
    const jwtService = inject(JwtService)
            
    const jwt = jwtService.getToken()

    if (!jwt) {
        //If token doesn't exist
        console.log("If token doesn't exist...")
        router.navigate(['/login']);
        return false;
    } else if (route.params['id'] != jwtService.decodeToken(jwt).id) {
        //If token exists but id is wrong -> redirect to login -> redirect to correct id
        console.log("If params wrong...")
        router.navigate(['/login']);
        return false;            
    } else {
        console.log("If token exists...")
        return true;
    }
}