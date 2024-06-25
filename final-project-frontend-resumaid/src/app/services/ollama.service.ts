import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { JwtService } from './jwt.service';

@Injectable()
export class OllamaService {

  urlExt: string = '/api/ollama'
  headers = new HttpHeaders().set("Authorization", "Bearer " + this.jwtSvc.getToken())

  constructor(private http: HttpClient, private jwtSvc: JwtService) { }

  getImprovement(message: string) {
    console.log("INSIDE OLLAMA SERVICE GET IMPROVEMENT")
    return this.http.post<Object>(`${this.urlExt}`, message, {headers: this.headers})
  }
}
