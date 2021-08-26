import { Component, OnInit } from '@angular/core';
import { FretCard, JamCard, PreviewService } from '../preview.service';

@Component({
  selector: 'app-caged',
  templateUrl: './caged.component.html',
  styleUrls: ['./caged.component.css']
})
export class CagedComponent implements OnInit {

  fretCards: FretCard[];
  fretError: any;
  jamCards: JamCard[];
  jamError: any;
  constructor(private previewService: PreviewService) { }

  ngOnInit(): void {
    this.previewService.getCaged5ArpFretDeck().subscribe(
      (data: FretCard[]) => this.fretCards = data, // success path
      error => this.fretError = error // error path
    );
    this.previewService.getCaged4ModesJamDeck().subscribe(
      (data: JamCard[]) => this.jamCards = data, // success path
      error => this.jamError = error // error path
    );
  }

}
