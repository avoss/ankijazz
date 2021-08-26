import { Component, OnInit } from '@angular/core';
import { ModesPractice, PreviewService, AbstractPreviewCarouselComponent } from '../preview.service';

@Component({
  selector: 'app-scales',
  templateUrl: './scales.component.html',
  styleUrls: ['./scales.component.css']
})
export class ScalesComponent extends AbstractPreviewCarouselComponent implements OnInit {
  cards: ModesPractice[];
  error: any;

  constructor(private previewService: PreviewService) { super(); }

  ngOnInit(): void {
    this.previewService.getModesPracticeCDeck().subscribe(
      (data: ModesPractice[]) => this.cards = data, // success path
      error => this.error = error // error path
    );
  }
}
