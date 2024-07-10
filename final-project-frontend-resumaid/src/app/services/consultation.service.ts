import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { JwtService } from './jwt.service';
import { Booking } from '../models/booking';

@Injectable()
export class ConsultationService {

  urlCalExt: string = '/api/cal'

  headers = new HttpHeaders().set("Authorization", "Bearer " + this.jwtSvc.getToken())

  constructor(private http: HttpClient, private jwtSvc: JwtService) { }

  getAvailableBookings(startDate: string, endDate: string) {
    const params = new HttpParams().set('startDate', startDate).set('endDate', endDate)
    console.log("GET AVAIL BOOKINGS")
    return this.http.get<any>(`${this.urlCalExt}/available`, {params, headers: this.headers})
  }

  postBooking(id: string, userId: string) {
    return this.http.post<boolean>(`${this.urlCalExt}/book/${userId}`, id, {headers: this.headers})
  }

  getBookingsByUser(userId: string) {
    return this.http.get<Booking[]>(`${this.urlCalExt}/all/${userId}`, {headers: this.headers}) 
  }
}
