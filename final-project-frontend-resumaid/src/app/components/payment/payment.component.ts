import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { loadStripe } from '@stripe/stripe-js';
import { JwtService } from '../../services/jwt.service';
import { StripeService } from '../../services/stripe.service';
import { ActivatedRoute } from '@angular/router';


@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrl: './payment.component.css'
})
export class PaymentComponent {

  userId!: string

  PUBLISHABLE_KEY = "pk_test_51PZ3XS2LmPZ6KWBAH7x7KJ8wGYTv4E268UjHxjBIfEszuxp6rUF15JR0dbksL1PxUxmfuDkNb6jbabdo0A8ABgp6000TX2Ie59"

  stripePromise = loadStripe(this.PUBLISHABLE_KEY);

  headers = new HttpHeaders().set("Authorization", "Bearer " + this.jwtSvc.getToken())

  constructor(private http: HttpClient, private stripeService: StripeService, private jwtSvc: JwtService, private activatedRoute: ActivatedRoute) { }

  async pay(): Promise<void> {

    const baseUrl = window.location.origin;
    
    this.userId = this.activatedRoute.snapshot.params["id"]

    // here we create a payment object
    const payment = {
      name: 'Resumaid Consultation',
      currency: 'sgd',
      // amount on cents *10 => to be on dollar
      amount: 5000,
      quantity: '1',
      cancelUrl: `${baseUrl}/#/cancel`,
      successUrl: `${baseUrl}/#/download/${this.userId}`,
    };

    const stripe = await this.stripePromise;

    // this is a normal http calls for a backend api
    this.http
      .post(`api/payment`, payment, {headers: this.headers})
      .subscribe((data: any) => {
        while (stripe == null) { }
        // I use stripe to redirect To Checkout page of Stripe platform
        stripe.redirectToCheckout({
          sessionId: data.id,
        });
      });
  }
}