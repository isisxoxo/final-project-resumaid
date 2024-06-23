import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { JwtService } from './jwt.service';

@Injectable()
export class ResumeService {

  urlExt: string = '/api/create'
  headers = new HttpHeaders().set("Authorization", "Bearer " + this.jwtSvc.getToken())

  constructor(private http: HttpClient, private jwtSvc: JwtService) { }

  saveNewResume(formData: FormData, userId: string) {
    return this.http.post<Object>(`${this.urlExt}/${userId}`, formData, {headers: this.headers})
  }
}
