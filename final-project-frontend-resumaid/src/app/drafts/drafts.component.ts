import { Component, OnInit } from '@angular/core';
import { ResumeService } from '../services/resume.service';
import { Observable } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { resume } from '../models/resume';
import swal from 'sweetalert';

@Component({
  selector: 'app-drafts',
  templateUrl: './drafts.component.html',
  styleUrl: './drafts.component.css'
})
export class DraftsComponent implements OnInit {

  getAllResume$!: Observable<resume[]>
  userId!: string
  result!: resume[]

  deleteResume$!: Observable<boolean>

  constructor(private resumeSvc: ResumeService, private activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {

    this.userId = this.activatedRoute.snapshot.params['id']

    this.getAllResume$ = this.resumeSvc.getAllResume(this.userId)
    this.getAllResume$.subscribe({
      next: data => {
        this.result = data
        console.log(">>>> GET RESULTS:", this.result)
      },
      error: error => console.error('Error getting resume:', error)
    })
  }

  deleteResumeById(id: string, title: string) {


    swal({
      title: "Are you sure you want to delete resume?",
      text: title,
      icon: "warning",
      buttons: [
        'No, cancel it!',
        'Yes, I am sure!'
      ],
      dangerMode: true,
    }).then((result) => {
      if (result){

        this.deleteResume$ = this.resumeSvc.deleteResumeById(id, this.userId)
        this.deleteResume$.subscribe({
          next: data => {
            location.reload() //Refresh page
          },
          error: error => console.error('Error deleting resume:', error)
        })
    
      }
    })


    // if (confirm("Are you sure you want to delete resume: " + title)) {
    //   this.deleteResume$ = this.resumeSvc.deleteResumeById(id, this.userId)
    //   this.deleteResume$.subscribe({
    //     next: data => {
    //       location.reload() //Refresh page
    //     },
    //     error: error => console.error('Error deleting resume:', error)
    //   })
    // }
  }
}