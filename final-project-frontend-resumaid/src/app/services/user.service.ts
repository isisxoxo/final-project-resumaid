import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { User } from '../models/user';
import { JwtService } from './jwt.service';

@Injectable()
export class UserService {

  urlExt: string = '/api'

  constructor(private http: HttpClient, private jwtService: JwtService) {}

  saveUser(user: User): Observable<HttpResponse<Object>> {
    return this.http.post<Object>(`${this.urlExt}/register`, user, { observe: 'response' }).pipe( //Include this to get full HttpResponse
      tap((response: HttpResponse<any>) => this.handleJwtResponse(response))
    );
  }

  getUserByEmailPw(user: User): Observable<HttpResponse<Object>> {
    return this.http.post<Object>(`${this.urlExt}/login`, user, { observe: 'response' }).pipe(
      tap((response: HttpResponse<any>) => this.handleJwtResponse(response))
    );
  }

  private handleJwtResponse(response: HttpResponse<any>): void {
    const jwtToken = response.headers.get('Authorization');
    if (jwtToken) {
      const token = jwtToken.split('Bearer ')[1];
      this.jwtService.setToken(token);
    }
  }

}
