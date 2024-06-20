import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { JwtService } from './services/jwt.service';
import { Router } from '@angular/router';

@Injectable()
export class jwtInterceptor implements HttpInterceptor {

  constructor(private jwtService: JwtService, private router: Router) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    console.log("I'M IN INTERCEPTOR!!!!!")

    const jwtToken = this.jwtService.getToken();

    // If token is present
    if (jwtToken) {

    console.log("TOKEN IS PRESENT!!!!!")

      // If token is expired
      if (this.isTokenExpired(jwtToken)) {

        console.log("TOKEN IS EXPIRED!!!!!")

        this.jwtService.removeToken(); // Remove token
        window.location.href = '/login'; // Redirect to login page
        return throwError(() => 'Token Expired');
      }

      console.log("TOKEN IS NOT YET EXPIRED!!!!!")

      // If token is not yet expired, add to Authorization header
      req = req.clone({
        setHeaders: {
          Authorization: `Bearer ${jwtToken}`,
        },
      });

      return next.handle(req).pipe(
        catchError((error: HttpErrorResponse) => {
          if (error.status === 401) {
            // Handle unauthorized error (e.g., redirect to login)
            this.jwtService.removeToken();
            this.router.navigate(['/login']); // Redirect to login page
          }
          return throwError(() => error);
        })
      );

    } else {

      console.log("TOKEN IS NOT PRESENT!!!!!")

      return next.handle(req);
    }
  }

  private isTokenExpired(token: string): boolean {
    const expiry = this.getExpiryTimeFromToken(token);
    return expiry ? Date.now() >= expiry * 1000 : true;
  }

  private getExpiryTimeFromToken(token: string): number | null {
    try {
      const jwtPayload = JSON.parse(atob(token.split('.')[1]));
      return jwtPayload.exp;
    } catch (error) {
      return null;
    }
  }
}