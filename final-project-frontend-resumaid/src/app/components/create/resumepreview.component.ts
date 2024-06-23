import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { ResumeStore } from '../../services/resume.store';

@Component({
  selector: 'app-resumepreview',
  templateUrl: './resumepreview.component.html',
  styleUrl: './resumepreview.component.css'
})
export class ResumepreviewComponent implements OnInit {

  savedResume$!: Observable<any>

  constructor(private resumeStore: ResumeStore) {
  }

  ngOnInit(): void {
    this.savedResume$ = this.resumeStore.getSavedResume
  }
}
