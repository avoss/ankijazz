import { Component, OnInit } from '@angular/core';
import { AbstractPreviewCarouselComponent, PreviewService, ModesTheory } from '../preview.service';

@Component({
  selector: 'app-theory',
  templateUrl: './theory.component.html',
  styleUrls: ['./theory.component.css']
})
export class TheoryComponent extends AbstractPreviewCarouselComponent implements OnInit {
  cards: ModesTheory[];
  error: any;

  constructor(private previewService: PreviewService) { super(); }

  ngOnInit(): void {
    this.previewService.getModesTheoryDeck().subscribe(
      (data: ModesTheory[]) => this.cards = data, // success path
      error => this.error = error // error path
    );
  }

}
