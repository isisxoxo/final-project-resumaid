import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-create',
  templateUrl: './create.component.html',
  styleUrl: './create.component.css'
})
export class CreateComponent implements OnInit {

  userId!: string

  constructor(private activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.userId = this.activatedRoute.snapshot.params['id']
  }

}
