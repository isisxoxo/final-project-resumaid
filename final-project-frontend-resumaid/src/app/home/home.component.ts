import { Component } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent {


  slides: string [] = [
    "Ever since I've started using ResumAId, I have not received a single rejection letter",
    "ResumAId consultants are the most professional and give the best advice!",
    "ResumAId is my go-to site when I want to update my resume"
  ]
  i=0;

  getSlide() {
      return this.slides[this.i];
  }

  getPrev() {
      this.i = this.i===0 ? 0 : this.i - 1;
  }
  
  getNext() {
    if (this.i != 2) {
      this.i = this.i===this.slides.length ? this.i : this.i + 1;
    }
  }
}
