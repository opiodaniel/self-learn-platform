import { TestBed } from '@angular/core/testing';

import { CoursecommentService } from './coursecomment.service';

describe('CoursecommentService', () => {
  let service: CoursecommentService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CoursecommentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
