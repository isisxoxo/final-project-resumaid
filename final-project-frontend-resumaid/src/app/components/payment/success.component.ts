import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ConsultationService } from '../../services/consultation.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-success',
  templateUrl: './success.component.html',
  styleUrl: './success.component.css'
})
export class SuccessComponent implements OnInit {

  userId!: string
  id!: string //meeting id
  resumeId!: string

  saveBooking$!: Observable<boolean>

  constructor(private activatedRoute: ActivatedRoute, private router: Router, 
              private consultationService: ConsultationService){
  }

  ngOnInit(): void {
    this.userId = this.activatedRoute.snapshot.params['id']
    this.id = this.activatedRoute.snapshot.queryParams['mid']
    this.resumeId = this.activatedRoute.snapshot.queryParams['q']

    if (this.userId && this.id && this.resumeId) {

      this.saveBooking$ = this.consultationService.postBooking(this.id, this.userId)
      this.saveBooking$.subscribe({
        next: data => {
          const result = data
          console.log(">>>> UPDATED Booking:", data)
        },
        error: error => console.error('Error updating booking:', error)
      })
    }
  }

}
