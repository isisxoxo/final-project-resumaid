import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';
import { Observable, Subject } from 'rxjs';


@Injectable()
export class JwtService {

  private jwtTokenSubject = new Subject<string | null>();

  constructor() {
  }

  setToken(jwtToken: string) {
    localStorage.setItem('jwtToken', jwtToken)
  }

  getToken(): string | null {
    const token = localStorage.getItem('jwtToken')
    this.jwtTokenSubject.next(token)
    return token
  }

  isLoggedIn(): Observable<any> {
    return this.jwtTokenSubject.asObservable()
  }

  removeToken() {
    localStorage.removeItem('jwtToken')
  }

  decodeToken(token: string): any {
    try {
      return jwtDecode(token);
    } catch (error) {
      console.error('Error decoding JWT:', error);
      return null;
    }
  }

  isTokenExpired(token: string): boolean {
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
