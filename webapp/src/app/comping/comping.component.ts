import { Component, OnInit } from '@angular/core';
import { JamCarouselComponent } from '../jam-carousel/jam-carousel.component';
import { JamCard, PreviewService, AbstractPreviewCarouselComponent } from '../preview.service';

@Component({
  selector: 'app-comping',
  templateUrl: './comping.component.html',
  styleUrls: ['./comping.component.css']
})
export class CompingComponent implements OnInit {
  compingCards: JamCard[];
  compingError: any;
  constructor(private previewService: PreviewService) { }

  ngOnInit(): void {
    this.previewService.getJamDeck().subscribe(
      (data: JamCard[]) => this.compingCards = data, // success path
      error => this.compingError = error // error path
    );
  }

}
