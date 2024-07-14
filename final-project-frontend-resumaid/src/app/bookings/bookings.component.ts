import { Component, OnInit } from '@angular/core';
import { Booking } from '../models/booking';
import { Observable } from 'rxjs';
import { ConsultationService } from '../services/consultation.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-bookings',
  templateUrl: './bookings.component.html',
  styleUrl: './bookings.component.css'
})
export class BookingsComponent implements OnInit {

  getAllBookings$!: Observable<Booking[]>
  userId!: string
  result: Booking[] = [];

  deleteResume$!: Observable<boolean>

  constructor(private consultationService: ConsultationService, private activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {

    this.userId = this.activatedRoute.snapshot.params['id']

    this.getAllBookings$ = this.consultationService.getBookingsByUser(this.userId)
    this.getAllBookings$.subscribe({
      next: data => {
        this.result = data
        console.log(">>>> GET RESULTS:", this.result)
      },
      error: error => console.error('Error getting bookings:', error)
    })
  }
}
