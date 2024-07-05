import { Component, OnInit } from '@angular/core';
import { ResumeService } from '../services/resume.service';
import { Observable } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { resume } from '../models/resume';

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

    if (confirm("Are you sure you want to delete resume: " + title)) {
      this.deleteResume$ = this.resumeSvc.deleteResumeById(id, this.userId)
      this.deleteResume$.subscribe({
        next: data => {
          alert("Resume deleted for " + title)
          location.reload() //Refresh pag
        },
        error: error => console.error('Error deleting resume:', error)
      })
    }
  }
}