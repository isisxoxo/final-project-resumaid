import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import html2canvas from 'html2canvas';
import jspdf from 'jspdf';
import { ResumeService } from '../../services/resume.service';
import { Observable } from 'rxjs';
import { resume } from '../../models/resume';
import { ResumeStore } from '../../services/resume.store';
import { ImageStore } from '../../services/image.store';

@Component({
  selector: 'app-download',
  templateUrl: './download.component.html',
  styleUrl: './download.component.css'
})
export class DownloadComponent implements OnInit {
  id!: string
  userId!: string
  data!: any
  resume!: resume
  
  getResumeById$!: Observable<resume>
  
  constructor(private activatedRoute: ActivatedRoute, private resumeSvc: ResumeService, 
              private resumeStore: ResumeStore, private imgStore: ImageStore) {
  }

  ngOnInit(): void {
    this.userId = this.activatedRoute.snapshot.params['id']
    this.id = this.activatedRoute.snapshot.queryParams['q']

    this.getResumeById$ = this.resumeSvc.getResumeById(this.id, this.userId)
    this.getResumeById$.subscribe({
      next: data => {
        this.resume = data;
        this.resumeStore.saveResume(this.resume) //Retrieve and save resume to store for preview
        this.imgStore.saveImgUrl(this.resume.url) //Save to image store
      },
      error: error => console.error('Error getting resume:', error)
    })
  }

  public convertToPDF() {
    this.data = document.getElementById('contentToConvert')

    html2canvas(this.data, {allowTaint: true, useCORS: true}).then(canvas => {
      var imgWidth = 210;
      var pageHeight = 295;
      var imgHeight = canvas.height * imgWidth / canvas.width;
      var heightLeft = imgHeight;
      
      const contentDataURL = canvas.toDataURL('image/png')
      let pdf = new jspdf('p', 'mm', 'a4'); // A4 size page of PDF
      var position = 0;
      pdf.addImage(contentDataURL, 'PNG', 0, position, imgWidth, imgHeight)
      pdf.save(this.resume.title); // Generated PDF
    });
  }
}
