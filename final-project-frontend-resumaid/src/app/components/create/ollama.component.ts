import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { Observable } from 'rxjs';
import { OllamaService } from '../../services/ollama.service';

@Component({
  selector: 'app-ollama',
  templateUrl: './ollama.component.html',
  styleUrl: './ollama.component.css'
})
export class OllamaComponent implements OnChanges {

  @Input()
  message!: string

  improvedPoints$!: Observable<Object>

  response!: string
  
  constructor(private ollamaSvc: OllamaService) {
  }


  ngOnChanges(changes: SimpleChanges): void {

    console.log(">>>>>> CHANGES:", changes)

    this.response = ""

    console.log("NGONCHANGES", this.message)

    this.improvedPoints$ = this.ollamaSvc.getImprovement(this.message)
    
    this.improvedPoints$.subscribe({
      next: data => {
        const result = data as {message: string}
        
        console.log("RESPONSE FROM OLLAMA:", result)

        const message = result['message']
        let modResult = ""
        let modResult2 = ""

        if(message.includes("\n\n")) {
          console.log("INCLUDES \n\n")
          modResult = message.split("\n\n")[1]
        } else {
          modResult = message
        }
        
        if(modResult.includes("\n")) {
          console.log("INCLUDES \n")
          modResult2 = modResult.replaceAll("\n", "")
        } else {
          modResult2 = modResult
        }
        
        this.response = modResult2

      },
      error: error => console.error('Error getting Ollama:', error)
    })
  }

}
