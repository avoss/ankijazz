import { HttpClient } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompingComponent } from './comping.component';

describe('CompingComponent', () => {
  let component: CompingComponent;
  let fixture: ComponentFixture<CompingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CompingComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    TestBed.configureTestingModule({imports: [ HttpClientTestingModule ]});
    TestBed.inject(HttpClient);
    TestBed.inject(HttpTestingController);    

    fixture = TestBed.createComponent(CompingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
