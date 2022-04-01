import { HttpClient } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RhythmComponent } from './rhythm.component';


describe('RhythmComponent', () => {
  let component: RhythmComponent;
  let fixture: ComponentFixture<RhythmComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RhythmComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    TestBed.configureTestingModule({imports: [ HttpClientTestingModule ]});
    TestBed.inject(HttpClient);
    TestBed.inject(HttpTestingController);    

    fixture = TestBed.createComponent(RhythmComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
