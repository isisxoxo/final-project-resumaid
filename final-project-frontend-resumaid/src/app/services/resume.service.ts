import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { JwtService } from './jwt.service';
import { resume } from '../models/resume';

@Injectable()
export class ResumeService {

  urlCreateExt: string = '/api/create'
  urlViewExt: string = '/api/view'
  urlDelExt: string = '/api/delete'

  headers = new HttpHeaders().set("Authorization", "Bearer " + this.jwtSvc.getToken())

  constructor(private http: HttpClient, private jwtSvc: JwtService) { }

  saveNewResume(userId: string, formData: FormData) {
    return this.http.post<Object>(`${this.urlCreateExt}/${userId}`, formData, {headers: this.headers})
  }

  getAllResume(userId: string) {
    return this.http.get<resume[]>(`${this.urlViewExt}/${userId}/all`, {headers: this.headers})
  }

  getResumeById(id: string, userId: string) {
    const params = new HttpParams().set('id', id)
    return this.http.get<resume>(`${this.urlViewExt}/${userId}`, {params, headers: this.headers})

  }

  updateResumeById(id: string, userId: string, formData: FormData) {
    const params = new HttpParams().set('id', id)
    return this.http.put<boolean>(`${this.urlCreateExt}/${userId}`, formData, {params, headers: this.headers})
  }

  deleteResumeById(id: string, userId: string) {
    const params = new HttpParams().set('id', id)
    return this.http.delete<boolean>(`${this.urlDelExt}/${userId}`, {params, headers: this.headers})
  }
}
