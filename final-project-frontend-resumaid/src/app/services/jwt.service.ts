import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';


@Injectable()
export class JwtService {

  constructor() { }

  setToken(jwtToken: string) {
    localStorage.setItem('jwtToken', jwtToken)
  }

  getToken(): string | null {
    return localStorage.getItem('jwtToken')
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
