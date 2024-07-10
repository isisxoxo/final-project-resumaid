import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { JwtService } from '../services/jwt.service';

@Component({
  selector: 'app-toolbar',
  templateUrl: './toolbar.component.html',
  styleUrl: './toolbar.component.css'
})
export class ToolbarComponent implements OnInit, OnDestroy {

  loggedIn!: string

  constructor(private jwtSvc: JwtService, private router: Router) {}

  ngOnInit(): void {
    console.log("TOOLBAR RE-RENDERING")
    this.jwtSvc.isLoggedIn().subscribe(
      token => {
        this.loggedIn = token
      }
    )
  }

  ngOnDestroy(): void {
    this.jwtSvc.isLoggedIn().subscribe().unsubscribe();
  }

  clickMain() {
    let url = window.location.href;
    let userId = url.slice(-8) //Retrieve userId (8 characters substring UUID)
    this.router.navigate(['/main', userId])
  }

  clickCreate() {
    let url = window.location.href;
    let userId = url.slice(-8) //Retrieve userId (8 characters substring UUID)
    this.router.navigate(['/create', userId])
  }

  clickView() {
    let url = window.location.href;
    let userId = url.slice(-8) //Retrieve userId (8 characters substring UUID)
    this.router.navigate(['/view', userId])
  }

  clickBookings() {
    let url = window.location.href;
    let userId = url.slice(-8) //Retrieve userId (8 characters substring UUID)
    this.router.navigate(['/bookings', userId])
  }

  clickLogout() {
    this.jwtSvc.removeToken();
    this.router.navigate(['/login'])
  }
}
