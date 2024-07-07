import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, firstValueFrom, from } from 'rxjs';
import { JwtService } from './jwt.service';
import { Stripe, loadStripe } from '@stripe/stripe-js';

@Injectable()
export class StripeService {

  private stripePromise$: Promise<Stripe | null>;
  private stripeObservable$!: Observable<Stripe | null>;
  
  urlPaymentExt: string = '/api/payment'

  PUBLISHABLE_KEY = "pk_test_51PZ3XS2LmPZ6KWBAH7x7KJ8wGYTv4E268UjHxjBIfEszuxp6rUF15JR0dbksL1PxUxmfuDkNb6jbabdo0A8ABgp6000TX2Ie59"

  headers = new HttpHeaders().set("Authorization", "Bearer " + this.jwtSvc.getToken())

  constructor(private http: HttpClient, private jwtSvc: JwtService) {
    this.stripePromise$ = loadStripe(this.PUBLISHABLE_KEY)
    this.stripeObservable$ = from(this.stripePromise$)
  }

  loadStripe(): Observable<Stripe | null> {
    return this.stripeObservable$;
  }

  createCharge(token: string, amount: number, currency: string, description: string): Observable<any> {
    return this.http.post<any>(`${this.urlPaymentExt}/charge`, { token, amount, currency, description, headers: this.headers });
  }

}
