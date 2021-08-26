import { Component, OnInit } from '@angular/core';
import { FretCard, JamCard, PentaChord, PreviewService } from '../preview.service';

@Component({
  selector: 'app-pentatonics',
  templateUrl: './pentatonics.component.html',
  styleUrls: ['./pentatonics.component.css']
})
export class PentatonicsComponent implements OnInit {

  fretCards: FretCard[];
  fretError: any;

  jamCards: JamCard[];
  jamError: any;
  
  pentaChords: PentaChord[];
  pentaError: any;

  constructor(private previewService: PreviewService) { }

  ngOnInit(): void {
    this.previewService.getPenta3ChordsFretDeck().subscribe(
      (data: FretCard[]) => this.fretCards = data, // success path
      error => this.fretError = error // error path
    );
    this.previewService.getPenta4ChordsJamDeck().subscribe(
      (data: JamCard[]) => this.jamCards = data, // success path
      error => this.jamError = error // error path
    );
    this.previewService.getPentaChords().subscribe(
      (data: PentaChord[]) => this.pentaChords = data, // success path
      error => this.pentaError = error // error path
    );
  }

}
