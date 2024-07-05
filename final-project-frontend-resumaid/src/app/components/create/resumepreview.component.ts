import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { ResumeStore } from '../../services/resume.store';
import { ImageStore } from '../../services/image.store';

@Component({
  selector: 'app-resumepreview',
  templateUrl: './resumepreview.component.html',
  styleUrl: './resumepreview.component.css'
})
export class ResumepreviewComponent implements OnInit {

  savedResume$!: Observable<any>
  savedImgUrl$!: Observable<string>
  previewUrl!: string | ArrayBuffer | null

  constructor(private resumeStore: ResumeStore, private imageStore: ImageStore) {
  }

  ngOnInit(): void {
    console.log("IN RESUME PREVIEW!")

    this.savedResume$ = this.resumeStore.getSavedResume
    this.savedImgUrl$ = this.imageStore.getSavedImgUrl

    this.savedResume$.subscribe(
      data => {
        if(data.fileSource) {
          //PREVIEW IMAGE
          console.log("DATA.MYPIC", data.fileSource)
          const reader = new FileReader();
          reader.onload = () => {
            console.log(">>>> READER:", reader)
            this.previewUrl = reader.result;
          }
          reader.readAsDataURL(data.fileSource);
        } else {
          this.previewUrl = null
        }
      })

      console.log("PREVIEW URL:",this.previewUrl)
  }
}
