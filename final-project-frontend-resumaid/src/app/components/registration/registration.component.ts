import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { Observable } from 'rxjs';
import { User } from '../../models/user';
import { Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { JwtService } from '../../services/jwt.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrl: './registration.component.css'
})
export class RegistrationComponent implements OnInit {

  form!: FormGroup
  saveUser$!: Observable<HttpResponse<Object>>
  error: boolean = false
  errorMsg!: string
  passwordVisible = false //Cannot see pw at first
  
  constructor(private fb: FormBuilder, private userSvc: UserService, private jwtSvc: JwtService, private router: Router) {
  }

  ngOnInit(): void {
    const jwt = this.jwtSvc.getToken();

    if (jwt) {

      console.log("TOKEN EXISTS ALREADY UPON ONINIT: " + jwt)

      const decodedToken = this.jwtSvc.decodeToken(jwt)
      const id = decodedToken.id
      this.router.navigate(['/main', id])
    }
    this.form = this.fb.group({
      username: this.fb.control('', [Validators.required, Validators.minLength(3), Validators.maxLength(16)]),
      email: this.fb.control('', [Validators.required, Validators.email]),
      password: this.fb.control('', [Validators.required])
    })
  }


  onClickRevealPassword(event: any) {
    event.preventDefault(); // Prevent form submission and revealing the password when enter button is pressed
    if (event.pointerType) {
      this.passwordVisible = !this.passwordVisible;
    }
  } 


  onSubmit() {
    const user: User = this.form.value as User;
    
    this.saveUser$ = this.userSvc.saveUser(user)
    this.saveUser$.subscribe({
      next: (response: HttpResponse<Object>) => {
        const result = response.body as {id: string}
        const id = result.id
        this.router.navigate(['/main', id])
      },
      error: error => {
        this.error = true
        this.errorMsg = error.error.message
      }
    })
  }

}

