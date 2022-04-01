import { Component, OnInit } from '@angular/core';
import { RhythmCard, PreviewService, AbstractPreviewCarouselComponent } from '../preview.service';

@Component({
  selector: 'app-rhythm',
  templateUrl: './rhythm.component.html',
  styleUrls: ['./rhythm.component.css']
})
export class RhythmComponent extends AbstractPreviewCarouselComponent implements OnInit {
  cards: RhythmCard[];
  error: any;

  constructor(private previewService: PreviewService) { super(); }

  ngOnInit(): void {
    this.previewService.getRhythmDeck().subscribe(
      (data: RhythmCard[]) => this.cards = data, // success path
      error => this.error = error // error path
    );
  }

}
