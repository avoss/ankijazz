import { HttpClient } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PentatonicsComponent } from './pentatonics.component';

describe('PentatonicsComponent', () => {
  let component: PentatonicsComponent;
  let fixture: ComponentFixture<PentatonicsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PentatonicsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    TestBed.configureTestingModule({imports: [ HttpClientTestingModule ]});
    TestBed.inject(HttpClient);
    TestBed.inject(HttpTestingController);    
    fixture = TestBed.createComponent(PentatonicsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
