
export class Smpte {
    static fromMillis(millis: number, fps: number): Smpte {
        let remainingSeconds = millis / 1000;
        let hours = Math.floor(remainingSeconds / 3600);
        remainingSeconds -= hours * 3600;
        let minutes = Math.floor(remainingSeconds / 60);
        remainingSeconds -= minutes * 60;
        let seconds = Math.floor(remainingSeconds);
        remainingSeconds -= seconds;
        let frames = Math.round(remainingSeconds * fps);
        return new Smpte(hours, minutes, seconds, frames);
    }
    toMillis(fps: number): number {
        return (this.hours * 3600 + this.minutes * 60 + this.seconds + this.frames / fps) * 1000;
    }
    constructor(public hours = 0, public minutes = 0, public seconds = 0, public frames = 0) {

    }
}
