import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FretCarouselComponent } from './fret-carousel.component';

describe('FretCarouselComponent', () => {
  let component: FretCarouselComponent;
  let fixture: ComponentFixture<FretCarouselComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FretCarouselComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FretCarouselComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
