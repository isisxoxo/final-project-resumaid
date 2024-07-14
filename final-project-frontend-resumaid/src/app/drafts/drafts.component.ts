import { Component, OnInit, ViewChild } from '@angular/core';
import { ResumeService } from '../services/resume.service';
import { Observable } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { resume } from '../models/resume';
import swal from 'sweetalert';
import { MatSort, Sort } from '@angular/material/sort';

@Component({
  selector: 'app-drafts',
  templateUrl: './drafts.component.html',
  styleUrl: './drafts.component.css'
})
export class DraftsComponent implements OnInit {

  getAllResume$!: Observable<resume[]>
  userId!: string
  result: resume[] = [];

  deleteResume$!: Observable<boolean>

  sortedData!: resume[];

  constructor(private resumeSvc: ResumeService, private activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {

    this.userId = this.activatedRoute.snapshot.params['id']

    this.getAllResume$ = this.resumeSvc.getAllResume(this.userId)
    this.getAllResume$.subscribe({
      next: data => {
        this.result = data
        console.log(">>>> GET RESULTS:", this.result)
        this.sortedData = data.slice() //Shallow copy
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
  }


    // if (confirm("Are you sure you want to delete resume: " + title)) {
    //   this.deleteResume$ = this.resumeSvc.deleteResumeById(id, this.userId)
    //   this.deleteResume$.subscribe({
    //     next: data => {
    //       location.reload() //Refresh page
    //     },
    //     error: error => console.error('Error deleting resume:', error)
    //   })
    // }

    sortData(sort: Sort) {

      const data = this.result.slice(); //Shallow copy

      if (!sort.active || sort.direction === '') {
        this.sortedData = data; //Go back to original (default list)
        return;
      }

      this.sortedData = data.sort((a, b) => {
        const isAsc = sort.direction === 'asc';
        switch (sort.active) {
          case 'title':
            return compare(a.title, b.title, isAsc);
          case 'lastUpdateTime':
            return compare(a.lastUpdateTime, b.lastUpdateTime, isAsc);
          case 'creationTime':
            return compare(a.creationTime, b.creationTime, isAsc);
          default:
            return 0;
        }
      });

    }

}

function compare(a: Date | string, b: Date | string, isAsc: boolean) {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}