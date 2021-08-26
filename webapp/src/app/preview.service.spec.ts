// Http testing module and mocking controller
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

// Other imports
import { TestBed } from '@angular/core/testing';
import { PreviewService } from './preview.service';

describe('PreviewService', () => {
  let service: PreviewService;

  beforeEach(() => {
    TestBed.configureTestingModule({imports: [ HttpClientTestingModule ]});
    TestBed.inject(HttpClient);
    TestBed.inject(HttpTestingController);    
    service = TestBed.inject(PreviewService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
