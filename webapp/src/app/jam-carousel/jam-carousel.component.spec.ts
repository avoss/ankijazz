import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JamCarouselComponent } from './jam-carousel.component';

describe('JamCarouselComponent', () => {
  let component: JamCarouselComponent;
  let fixture: ComponentFixture<JamCarouselComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ JamCarouselComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(JamCarouselComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
