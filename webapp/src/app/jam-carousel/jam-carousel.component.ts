import { Component, Input, OnInit } from '@angular/core';
import { AbstractPreviewCarouselComponent, JamCard } from '../preview.service';

@Component({
  selector: 'app-jam-carousel',
  templateUrl: './jam-carousel.component.html',
  styleUrls: ['./jam-carousel.component.css']
})
export class JamCarouselComponent extends AbstractPreviewCarouselComponent implements OnInit {
  @Input() cards: JamCard[];
  @Input() error: any;
  @Input() titlePrefix: string;

  constructor() { super(); }

  ngOnInit(): void {
  }

}
