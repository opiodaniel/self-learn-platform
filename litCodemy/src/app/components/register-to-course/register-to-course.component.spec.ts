import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterToCourseComponent } from './register-to-course.component';

describe('RegisterToCourseComponent', () => {
  let component: RegisterToCourseComponent;
  let fixture: ComponentFixture<RegisterToCourseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegisterToCourseComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RegisterToCourseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
