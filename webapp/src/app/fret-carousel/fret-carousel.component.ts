import { Component, Input, OnInit } from '@angular/core';
import { AbstractPreviewCarouselComponent, FretCard } from '../preview.service';

@Component({
  selector: 'app-fret-carousel',
  templateUrl: './fret-carousel.component.html',
  styleUrls: ['./fret-carousel.component.css']
})
export class FretCarouselComponent extends AbstractPreviewCarouselComponent implements OnInit {
  @Input() cards: FretCard[];
  @Input() error: any;
  @Input() titlePrefix: string;
  constructor() { super(); }

  ngOnInit(): void {
  }
}
