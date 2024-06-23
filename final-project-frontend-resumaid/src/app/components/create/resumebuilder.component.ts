import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { ResumeService } from '../../services/resume.service';
import { ResumeStore } from '../../services/resume.store';

@Component({
  selector: 'app-resumebuilder',
  templateUrl: './resumebuilder.component.html',
  styleUrl: './resumebuilder.component.css'
})
export class ResumebuilderComponent implements OnInit {

  userId!: string

  resumeForm!: FormGroup
  fileName: string = ''
  previewUrl!: string | ArrayBuffer | null;
  educationArray!: FormArray
  workArray!: FormArray
  ccaArray!: FormArray

  saveNewResume$!: Observable<Object>

  constructor(private fb: FormBuilder, private router: Router, private resumeStore: ResumeStore,
    private activatedRoute: ActivatedRoute, private resumeSvc: ResumeService) {
  }

  ngOnInit(): void {
    this.educationArray = this.fb.array([])
    this.workArray = this.fb.array([])
    this.ccaArray = this.fb.array([])

    this.resumeForm = this.fb.group({
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

    this.resumeForm.valueChanges.subscribe(data => this.resumeStore.saveResume(data));
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
  }

  onFileSelect(event: any) {
    if(event.target.files.length > 0) {
      const file = event.target.files[0]
      
      /* To get file name */
      this.fileName = file.name

      /* For file preview */
      const reader = new FileReader();
      reader.onload = () => {
        console.log(">>>> READER:", reader)
        this.previewUrl = reader.result;
      }
      reader.readAsDataURL(file);

      this.resumeForm.patchValue({
        fileSource: file
      })
    }

  }

  onSubmit() {

    const formData = new FormData();
    formData.append('fullName', this.resumeForm.get('fullname')?.value);
    formData.append('phone', this.resumeForm.get('phone')?.value);
    formData.append('email', this.resumeForm.get('email')?.value);
    formData.append('educationJson', JSON.stringify(this.resumeForm.get('education')?.value));
    formData.append('workJson', JSON.stringify(this.resumeForm.get('work')?.value));
    formData.append('ccaJson', JSON.stringify(this.resumeForm.get('cca')?.value));
    formData.append('additional', this.resumeForm.get('additional')?.value);

    const fileSourceValue = this.resumeForm.get('fileSource')?.value;
    console.log(fileSourceValue)
    if (fileSourceValue != null && fileSourceValue.size > 0 && fileSourceValue != undefined) {
      formData.append('mypic', fileSourceValue);
    }

    console.log(this.resumeForm.get('fullname')?.value) //Isis Lee Ming Wei
    
    console.log(this.resumeForm.get('phone')?.value) //97260788
    console.log(this.resumeForm.get('email')?.value); //isislee107@gmail.com
    console.log(JSON.stringify(this.resumeForm.get('education')?.value)); //[{"eName":"NUS","eCountry":"Singapore","eCert":"BAC","eFrom":"2024-06-12","eCurrent":false,"eTo":"2024-06-20","ePoints":"I love NUS\nSpecialise in BA"}]
    console.log(JSON.stringify(this.resumeForm.get('work')?.value)); //[{"wName":"Deutsche Bank","wCountry":"Singapore","wRole":"Trainee","wFrom":"2024-06-06","wCurrent":true,"wPoints":"IBF"}]
    console.log(JSON.stringify(this.resumeForm.get('cca')?.value)); //[{"cName":"NIL","cCountry":"Singapore","cTitle":"NIL","cFrom":"2024-06-13","cCurrent":false,"cTo":"","cPoints":"NIL"}]
    console.log(this.resumeForm.get('additional')?.value); //Saxophone\r\nHarp

    this.userId = this.activatedRoute.snapshot.params['id']

    console.log(">>>>> USERID:", this.userId)

    this.saveNewResume$ = this.resumeSvc.saveNewResume(formData, this.userId)
    this.saveNewResume$.subscribe({
      next: data => {

        /* RECEIVED AS STRING */
        // console.log(">>>> SAVED EMPLOYEE - image URL:", data)

        /* RECEIVED AS OBJECT */
        const result = data
        console.log(">>>> SAVED RESUME:", data)
        alert('File uploaded successfully!!!');
      },
      error: error => console.error('Error saving employee:', error)
    })

    this.router.navigate(['/']) //TO CHANGE TO NEXT PAGE (STEP 3)
  }
}
