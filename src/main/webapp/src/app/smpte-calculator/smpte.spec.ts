import { Smpte } from './smpte';

describe('Smpte', () => {
  it('should create an instance', () => {
    let smpte = new Smpte();
    expect(smpte).toBeTruthy();
    expect(smpte.hours).toEqual(0);
  });
  it('should convert to millis', () => {
    let smpte = new Smpte(1, 2, 3, 4);
    let fps = 25;
    expect(smpte.toMillis(fps)).toEqual((1 * 3600 + 2 * 60 + 3 + 4 / fps) * 1000);
  });
  it('should be created from millis', () => {
    let fps = 25;
    let millis = (1 * 3600 + 2 * 60 + 3 + 4 / fps) * 1000;
    let smpte : Smpte = Smpte.fromMillis(millis, fps);
    expect(smpte.hours).toEqual(1);
    expect(smpte.minutes).toEqual(2);
    expect(smpte.seconds).toEqual(3);
    expect(smpte.frames).toEqual(4);
  });
});
