import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';

@Component({
  selector: 'app-cancel',
  templateUrl: './cancel.component.html',
  styleUrl: './cancel.component.css'
})
export class CancelComponent implements OnInit {

  userId!: string

  constructor(private activatedRouter: ActivatedRoute) {}

  ngOnInit(): void {
    this.userId = this.activatedRouter.snapshot.params['id']
  }
}
