import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { ResumeService } from '../../services/resume.service';
import { ResumeStore } from '../../services/resume.store';
import { resume } from '../../models/resume';
import { ImageStore } from '../../services/image.store';
import swal from 'sweetalert';

@Component({
  selector: 'app-resumebuilder',
  templateUrl: './resumebuilder.component.html',
  styleUrl: './resumebuilder.component.css'
})
export class ResumebuilderComponent implements OnInit, OnDestroy {

  id!: string
  getResumeById$!: Observable<resume>

  userId!: string
  defaultTitle!: string

  resumeForm!: FormGroup
  fileName: string = ''
  previewUrl!: string | ArrayBuffer | null
  educationArray!: FormArray
  workArray!: FormArray
  ccaArray!: FormArray

  @ViewChild('inputFile')
  inputFile!: ElementRef;

  isEduOllama = false
  eduIndex!: number
  onEduOllamaClick!: string

  isWorkOllama = false
  workIndex!: number
  onWorkOllamaClick!: string

  isCcaOllama = false
  ccaIndex!: number
  onCcaOllamaClick!: string

  saveNewResume$!: Observable<Object>
  updateResumeById$!: Observable<boolean>

  constructor(private fb: FormBuilder, private router: Router, private resumeStore: ResumeStore,
    private activatedRoute: ActivatedRoute, private resumeSvc: ResumeService, private imageStore: ImageStore) {
  }

  ngOnInit(): void {

    this.imageStore.saveImgUrl('') //Reset imageStore
    this.resumeStore.saveResume('') //Reset imageStore

    this.userId = this.activatedRoute.snapshot.params['id']

    const currTime = new Date().getTime().toString()
    this.defaultTitle = `Untitled-${currTime}`

    this.educationArray = this.fb.array([])
    this.workArray = this.fb.array([])
    this.ccaArray = this.fb.array([])

    this.resumeForm = this.fb.group({
      title: this.fb.control(this.defaultTitle),
      fullname: this.fb.control('', [Validators.required]),
      phone: this.fb.control('', [Validators.required]),
      email: this.fb.control('', [Validators.required, Validators.email]),
      mypic: this.fb.control(''),
      fileSource: this.fb.control(''),
      education: this.educationArray,
      work: this.workArray,
      cca: this.ccaArray,
      additional: this.fb.control('')
    })


    this.id = this.activatedRoute.snapshot.queryParams['q']

    if (this.id) {
      //IF HAVE QUERY PARAM

      this.getResumeById$ = this.resumeSvc.getResumeById(this.id, this.userId)
      this.getResumeById$.subscribe({

        next: r => {

          this.resumeForm.patchValue({
            title: r.title,
            fullname: r.fullName,
            phone: r.phone,
            email: r.email,
            // mypic: this.fb.control(''),
            // fileSource: this.fb.control('')
            additional: r.additional
          })
          
          r.education.forEach(
            (edu: any) => {
              if (edu.eFrom) {
                edu.eFrom = new Date (edu.eFrom).toISOString().split('T')[0];
              }
              if (!edu.eCurrent && edu.eTo) {
                edu.eTo = new Date (edu.eTo).toISOString().split('T')[0];
              }
              this.educationArray.push(this.fb.group(edu));
            }
          )
          r.work.forEach(
            (work: any) => {
              if (work.wFrom) {
                work.wFrom = new Date (work.wFrom).toISOString().split('T')[0];
              }
              if (!work.wCurrent && work.wTo) {
                work.wTo = new Date (work.wTo).toISOString().split('T')[0];
              }
              this.workArray.push(this.fb.group(work));
            }
          )
          r.cca.forEach(
            (cca: any) => {
              if (cca.cFrom) {
                cca.cFrom = new Date (cca.cFrom).toISOString().split('T')[0];
              }
              if (!cca.cCurrent && cca.cTo) {
                console.log("CCA CTO:", cca.cTo)
                cca.cTo = new Date (cca.cTo).toISOString().split('T')[0];
              }
              this.ccaArray.push(this.fb.group(cca));
            }
          )

          this.imageStore.saveImgUrl(r.url)
        },
        error: error => console.error('Error getting resume:', error)
      })
    }

    this.resumeForm.valueChanges.subscribe(data => {
      this.resumeStore.saveResume(data)
    });
  }

  addNewEducation() {
    const educationItem = this.fb.group({
      eName: this.fb.control(''),
      eCountry: this.fb.control(''),
      eCert: this.fb.control(''),
      eFrom: this.fb.control(''),
      eCurrent: this.fb.control(false),
      eTo: this.fb.control(''),
      ePoints: this.fb.control('')
    })

    this.educationArray.push(educationItem)
  }

  isEChecked(event: any, i: number) {
    let isEChecked = event.target.checked; 
    if (isEChecked) {
      this.educationArray.at(i).get("eTo")?.disable();
    } else {
      this.educationArray.at(i).get("eTo")?.enable();
    }
  }

  deleteEducation(idx: number) {
    this.educationArray.removeAt(idx)
    this.isEduOllama = false
  }

  addNewWork() {
    const workItem = this.fb.group({
      wName: this.fb.control(''),
      wCountry: this.fb.control(''),
      wRole: this.fb.control(''),
      wFrom: this.fb.control(''),
      wCurrent: this.fb.control(false),
      wTo: this.fb.control(''),
      wPoints: this.fb.control('')
    })

    this.workArray.push(workItem)
  }

  isWChecked(event: any, i: number) {
    let isWChecked = event.target.checked; 
    if (isWChecked) {
      this.workArray.at(i).get("wTo")?.disable();
    } else {
      this.workArray.at(i).get("wTo")?.enable();
    }
  }

  deleteWork(idx: number) {
    this.workArray.removeAt(idx)
    this.isWorkOllama = false
  }

  addNewCca() {
    const ccaItem = this.fb.group({
      cName: this.fb.control(''),
      cCountry: this.fb.control(''),
      cTitle: this.fb.control(''),
      cFrom: this.fb.control(''),
      cCurrent: this.fb.control(false),
      cTo: this.fb.control(''),
      cPoints: this.fb.control('')
    })

    this.ccaArray.push(ccaItem)
  }

  isCChecked(event: any, i: number) {
    let isCChecked = event.target.checked; 
    if (isCChecked) {
      this.ccaArray.at(i).get("cTo")?.disable();
    } else {
      this.ccaArray.at(i).get("cTo")?.enable();
    }
  }

  deleteCca(idx: number) {
    this.ccaArray.removeAt(idx)
    this.isCcaOllama = false
  }

  onFileSelect(event: any) {
    if(event.target.files.length > 0) {
      const file = event.target.files[0]

      this.resumeForm.patchValue({
        fileSource: file
      })
    }
  }

  onFileDelete() {
    this.resumeForm.patchValue({
      fileSource: ''
    })
    this.inputFile.nativeElement.value = ''
  }

  /* OLLAMA */
  improveEducation(idx: number) {
    if (this.educationArray.at(idx).get('ePoints')?.value) { //Only call Ollama if got value
      this.isEduOllama = true
      this.eduIndex = idx
      this.onEduOllamaClick = this.educationArray.at(idx).get("ePoints")?.value
    }
  }

  improveWork(idx: number) {
    if (this.workArray.at(idx).get('wPoints')?.value) {
      this.isWorkOllama = true
      this.workIndex = idx
      this.onWorkOllamaClick = this.workArray.at(idx).get("wPoints")?.value
    }
  }

  improveCca(idx: number) {
    if (this.ccaArray.at(idx).get('cPoints')?.value) {
      this.isCcaOllama = true
      this.ccaIndex = idx
      this.onCcaOllamaClick = this.ccaArray.at(idx).get("cPoints")?.value
    }
  }

  onSubmit(buttonid: string) {

    const formData = new FormData();
    formData.append('title', this.resumeForm.get('title')?.value);
    formData.append('fullName', this.resumeForm.get('fullname')?.value);
    formData.append('phone', this.resumeForm.get('phone')?.value);
    formData.append('email', this.resumeForm.get('email')?.value);
    formData.append('educationJson', JSON.stringify(this.resumeForm.get('education')?.value));
    formData.append('workJson', JSON.stringify(this.resumeForm.get('work')?.value));
    formData.append('ccaJson', JSON.stringify(this.resumeForm.get('cca')?.value));
    formData.append('additional', this.resumeForm.get('additional')?.value);

    const fileSourceValue = this.resumeForm.get('fileSource')?.value;
    if (fileSourceValue != null && fileSourceValue.size > 0 && fileSourceValue != undefined) {
      formData.append('mypic', fileSourceValue);
    }

    console.log(this.resumeForm.get('title')?.value)
    console.log(this.resumeForm.get('fullname')?.value) //Isis Lee Ming Wei
    console.log(this.resumeForm.get('phone')?.value) //97260788
    console.log(this.resumeForm.get('email')?.value); //isislee107@gmail.com
    console.log(JSON.stringify(this.resumeForm.get('education')?.value)); //[{"eName":"NUS","eCountry":"Singapore","eCert":"BAC","eFrom":"2024-06-12","eCurrent":false,"eTo":"2024-06-20","ePoints":"I love NUS\nSpecialise in BA"}]
    console.log(JSON.stringify(this.resumeForm.get('work')?.value)); //[{"wName":"Deutsche Bank","wCountry":"Singapore","wRole":"Trainee","wFrom":"2024-06-06","wCurrent":true,"wPoints":"IBF"}]
    console.log(JSON.stringify(this.resumeForm.get('cca')?.value)); //[{"cName":"NIL","cCountry":"Singapore","cTitle":"NIL","cFrom":"2024-06-13","cCurrent":false,"cTo":"","cPoints":"NIL"}]
    console.log(this.resumeForm.get('additional')?.value); //Saxophone\r\nHarp


    /* DIFFERENT SAVE BUTTONS */

    //SAVE AS EXISTING COPY
    if(buttonid == "save") {

      if (!this.id) {
        //NO QUERY PARAM = SAVE
        this.saveNewResume$ = this.resumeSvc.saveNewResume(this.userId, formData)
        this.saveNewResume$.subscribe({
          next: data => {
  
            /* RECEIVED AS STRING */
            // console.log(">>>> SAVED EMPLOYEE - image URL:", data)
  
            /* RECEIVED AS OBJECT */
            const result = data as {id: string}
            this.id = result.id
            console.log(">>>> SAVED RESUME:", this.id)
            // alert('File uploaded successfully!!!');
            swal( "Success", "New Resume Created Successfully!" , "success" );
  
            const queryParams = {q: this.id}
            console.log(this.id)
            this.router.navigate(['/consult', this.userId], {queryParams})
          },
          error: error => console.error('Error saving employee:', error)
        })
      } else {
        //HAVE QUERY PARAM = UPDATE
        this.updateResumeById$ = this.resumeSvc.updateResumeById(this.id, this.userId, formData)
        this.updateResumeById$.subscribe({
          next: data => {
            const result = data
            console.log(">>>> UPDATED RESUME:", data)
            // alert('File uploaded successfully!!!');
          swal ( "Success", "Resume Saved Successfully!" , "success" );
  
            const queryParams = {q: this.id}
            console.log(this.id)
            this.router.navigate(['/consult', this.userId], {queryParams})
          },
          error: error => console.error('Error updating employee:', error)
        })
      }
    }

    //SAVE AS A NEW COPY
    if (buttonid == "new") {
      this.saveNewResume$ = this.resumeSvc.saveNewResume(this.userId, formData)
      this.saveNewResume$.subscribe({
        next: data => {

          /* RECEIVED AS STRING */
          // console.log(">>>> SAVED EMPLOYEE - image URL:", data)

          /* RECEIVED AS OBJECT */
          const result = data as {id: string}
          this.id = result.id
          console.log(">>>> SAVED RESUME:", this.id)
          // alert('File uploaded successfully!!!');
          swal( "Success", "New Resume Created Successfully!" , "success" );

          const queryParams = {q: this.id}
          console.log(this.id)
          this.router.navigate(['/consult', this.userId], {queryParams})
        },
        error: error => console.error('Error saving employee:', error)
      })
    }

  }


  ngOnDestroy(): void {
    this.resumeForm.reset()
  }

}
