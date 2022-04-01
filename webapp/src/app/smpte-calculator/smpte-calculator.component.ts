import { Component, OnInit } from '@angular/core';
import { Smpte } from './smpte';

@Component({
  selector: 'app-smpte-calculator',
  templateUrl: './smpte-calculator.component.html',
  styleUrls: ['./smpte-calculator.component.css']
})
export class SmpteCalculatorComponent implements OnInit {

  framesPerSecond = 25;
  start = new Smpte(0, 0, 0, 0);
  end = new Smpte(0, 0, 5, 0);
  length = new Smpte(0, 0, 0, 0);

  beatsPerBar = 4;

  fixedBars = 2;
  fixedBeats = 2;
  computedBpm : number;

  fixedBpm = 120;
  computedBars :number;
  computedBeats :number;

  constructor() { }

  ngOnInit(): void {
    this.calculate();
  }

  calculate(): void {
    console.log("Calculating");
    const startMillis = this.start.toMillis(this.framesPerSecond);
    const endMillis = this.end.toMillis(this.framesPerSecond);
    const lengthInMillis = endMillis - startMillis;
    this.length = Smpte.fromMillis(lengthInMillis, this.framesPerSecond);
    const lengthInMinutes = lengthInMillis / 1000 / 60;

    const totalFixedBeats =  this.fixedBars * this.beatsPerBar + this.fixedBeats;
    this.computedBpm = totalFixedBeats / lengthInMinutes;

    const totalComputedBeats = this.fixedBpm * lengthInMinutes;
    this.computedBars = Math.floor(totalComputedBeats / this.beatsPerBar);
    this.computedBeats = totalComputedBeats - this.computedBars * this.beatsPerBar;
  }

  private framesToSeconds(frames : number) {
    return frames / this.framesPerSecond;
  }

  private secondsToFrames(seconds: number) {
    return seconds * this.framesPerSecond;
  }

}
