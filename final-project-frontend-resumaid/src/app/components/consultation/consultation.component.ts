import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { ConsultationService } from '../../services/consultation.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Booking } from '../../models/booking';

@Component({
  selector: 'app-consultation',
  templateUrl: './consultation.component.html',
  styleUrl: './consultation.component.css'
})
export class ConsultationComponent implements OnInit {

  availableBookings$!: Observable<any>
  dateForm!: FormGroup
  userId!: string
  id!: string

  constructor(private fb: FormBuilder, private consultationSvc: ConsultationService, 
            private router: Router, private activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.dateForm = this.fb.group({
      startDate: this.fb.control('', [Validators.required]),
      endDate: this.fb.control('', [Validators.required])
    })

    this.userId = this.activatedRoute.snapshot.params["id"]
    this.id = this.activatedRoute.snapshot.queryParams["q"]
  }

  onSubmit() {
    const currdate = new Date()
    let startdate = new Date(this.dateForm.get("startDate")?.value)
    // Only start from current date onwards
    if (startdate < currdate) {
      startdate = currdate
    }
    const enddate = new Date(this.dateForm.get("endDate")?.value)
    enddate.setHours(23, 59, 59, 999);

    console.log("START DATE:", startdate.toISOString())
    console.log("END DATE:", enddate.toISOString())

    this.availableBookings$ = this.consultationSvc.getAvailableBookings(startdate.toISOString(), enddate.toISOString())
    this.availableBookings$.subscribe(
      data => {
        console.log(">>>>> DATA:", data)
      }
      )
  }

  clickBooking(b: Booking) {

    const queryParams = {id: b.id, q: this.id}
    console.log("RESUME ID:", this.id)
    console.log("MEETING ID:", b.id)
    this.router.navigate(['/payment', this.userId], {queryParams})
  }

  skip() {
    const queryParams = {q: this.id}
    console.log(this.id)
    this.router.navigate(['/download', this.userId], {queryParams})
  }
}
