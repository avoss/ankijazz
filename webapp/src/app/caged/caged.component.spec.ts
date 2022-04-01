import { HttpClient } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CagedComponent } from './caged.component';

describe('CagedComponent', () => {
  let component: CagedComponent;
  let fixture: ComponentFixture<CagedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CagedComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    TestBed.configureTestingModule({imports: [ HttpClientTestingModule ]});
    TestBed.inject(HttpClient);
    TestBed.inject(HttpTestingController);    
    fixture = TestBed.createComponent(CagedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
