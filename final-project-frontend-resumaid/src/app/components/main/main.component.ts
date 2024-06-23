import { Component, OnInit } from '@angular/core';
import { JwtService } from '../../services/jwt.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrl: './main.component.css'
})
export class MainComponent implements OnInit {
  
  id!: string
  
  constructor(private jwtSvc: JwtService, private router: Router, private activatedRoute: ActivatedRoute){
  }

  ngOnInit(): void {
    this.id = this.activatedRoute.snapshot.params['id']
  }

  logout() {
    this.jwtSvc.removeToken();
    this.router.navigate(['/login'])
  }
}
