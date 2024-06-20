import { Component } from '@angular/core';
import { JwtService } from '../../services/jwt.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrl: './main.component.css'
})
export class MainComponent {
  
  constructor(private jwtSvc: JwtService, private router: Router){
  }

  logout() {
    this.jwtSvc.removeToken();
    this.router.navigate(['/login'])
  }
}
