import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';
import { User } from '../../models/user';
import { HttpResponse } from '@angular/common/http';
import { JwtService } from '../../services/jwt.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {

  form!: FormGroup
  loginUser$!: Observable<HttpResponse<Object>>
  error: boolean = false
  errorMsg!: string
  passwordVisible = false //Cannot see pw at first
  
  constructor(private fb: FormBuilder, private userSvc: UserService, private jwtSvc: JwtService, private router: Router) {
  }
  
  ngOnInit(): void {
    
    const jwt = this.jwtSvc.getToken();

    if (jwt) {

      console.log("TOKEN EXISTS ALREADY UPON ONINIT")
      const decodedToken = this.jwtSvc.decodeToken(jwt)
      console.log(decodedToken)
      const id = decodedToken.id
      this.router.navigate(['/main', id])
    }
    
    this.form = this.fb.group({
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

    this.loginUser$ = this.userSvc.getUserByEmailPw(user)
    this.loginUser$.subscribe({
      next: (response: HttpResponse<Object>) => {
        const result = response.body as {jwt: string, id: string}
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
