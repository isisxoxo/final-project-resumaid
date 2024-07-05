import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { ConsultationService } from '../../services/consultation.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Booking } from '../../models/booking';
import { ActivatedRoute, RouterLink } from '@angular/router';

@Component({
  selector: 'app-consultation',
  templateUrl: './consultation.component.html',
  styleUrl: './consultation.component.css'
})
export class ConsultationComponent implements OnInit {

  availableBookings$!: Observable<any>
  dateForm!: FormGroup
  id!: string

  constructor(private fb: FormBuilder, private consultationSvc: ConsultationService, private activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.dateForm = this.fb.group({
      startDate: this.fb.control('', [Validators.required]),
      endDate: this.fb.control('', [Validators.required])
    })

    this.id = this.activatedRoute.snapshot.params["id"]
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

    this.availableBookings$ = this.consultationSvc.getAvailableBookings(startdate.toISOString(), enddate.toISOString())
    this.availableBookings$.subscribe(
      data => {
        console.log(">>>>> DATA:", data)
      }
      )
  }
}
