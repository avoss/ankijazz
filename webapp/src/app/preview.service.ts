import { HttpClient } from '@angular/common/http';
import { Injectable, Input } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';

export interface ModesTheory {
  front: string;
  back: string;
  task: string;
  parentName: string;
  parentType: string;
  parentRoot: string;
  modeName: string;
  modeType: string;
  modeRoot: string;
}

export interface ModesPractice {
  modeName: string;
  modeType: string;
  modeRoot: string;
  parentName: string;
  parentType: string;
  parentRoot: string;
  modeMp3: string;
  modePng: string;
  direction: 'Ascending' | 'Descending';
  difficulty: number;
  positionPng?: string;
  position?: 'High' | 'Medium' | 'Low';
}

export interface JamCard {
    title: string;
    comment: string;
    type: string;
    key: string;
    style: string;
    tempo: number;
    songPng: string;
    songMp3: string;
    difficulty: number;
    positionLabel?: string;
    positionImage?: string;
    stringSet?: string;
}

export interface FretCard {
  title: string;
  comment: string;
  fretNumber: string;
  stringNumber: string;
  scaleName: string;
  scaleTypeName: string;
  scaleRootName: string;
  chordName: string;
  chordTypeName: string;
  chordRootName: string;
  frontPngName: string;
  backPngName: string;
  backMp3Name: string;
}

export interface RhythmCard {
  title: string;
  type: string;
  rhythmImage: string;
  rhythmAudio: string;
  metronomeAudio: string;
  hasTies: string;
  hasSyncopation: string;
  numberOfUniqueQuarters: number;
  tempo: number;
  difficulty: number;
}

export interface PentaChord {
  chordName: string;
  pentaName: string;
  commonNotes: string;
  extraNotes: string;
  missingNotes: string;

}

export abstract class AbstractPreviewCarouselComponent {
  readonly cycleInterval = 5000;
  readonly pauseInterval = 1000000;
  showNavigationArrows = true;
  interval = this.cycleInterval;

  onPlayerPlay(): void {
    console.log('onPlayerPlay()');
    this.showNavigationArrows = false;
    this.interval = this.pauseInterval;
  }
  onPlayerPause(): void {
    console.log('onPlayerPause()');
    this.showNavigationArrows = true;
    this.interval = this.cycleInterval;
  }
  onPlayerEnded(): void {
    console.log('onPlayerEnded()');
    this.showNavigationArrows = true;
    this.interval = this.cycleInterval;
  }

}

@Injectable({
  providedIn: 'root'
})
export class PreviewService {

  constructor(private http: HttpClient) { }

  getModesPracticeCDeck(): Observable<ModesPractice[]> {
    return this.http.get<ModesPractice[]>('assets/preview/ModesPracticeCDeck.json');
  }
  getModesTheoryDeck(): Observable<ModesTheory[]> {
    return this.http.get<ModesTheory[]>('assets/preview/ModesTheoryDeck.json');
  }
  getJamDeck(): Observable<JamCard[]> {
    return this.http.get<JamCard[]>('assets/preview/JamCGuitarDeck.json');
  }
  getRhythmDeck(): Observable<RhythmCard[]> {
    return this.http.get<RhythmCard[]>('assets/preview/RhythmPiano100Deck.json');
  }

  getCaged1ScaleFretDeck(): Observable<FretCard[]> {
    return this.http.get<FretCard[]>('assets/preview/Caged1ScalesFretboards.json');
  }
  getCaged4ModesJamDeck(): Observable<JamCard[]> {
    return this.http.get<JamCard[]>('assets/preview/Caged4ModesJamtracks.json');
  }
  getCaged5ArpFretDeck(): Observable<FretCard[]> {
    return this.http.get<FretCard[]>('assets/preview/Caged5ArpeggiosFretboards.json');
  }

  getPenta3ChordsFretDeck(): Observable<FretCard[]> {
    return this.http.get<FretCard[]>('assets/preview/Pentatonic3ChordsFretboards.json');
  }
  getPenta4ChordsJamDeck(): Observable<JamCard[]> {
    return this.http.get<JamCard[]>('assets/preview/Pentatonic4ChordsJamtracks.json');
  }
  getPentaChords(): Observable<PentaChord[]> {
    return this.http.get<PentaChord[]>('assets/PentatonicSubstitutions.json');
  }

}
