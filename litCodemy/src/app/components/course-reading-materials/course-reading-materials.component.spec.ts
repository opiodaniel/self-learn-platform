import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CourseReadingMaterialsComponent } from './course-reading-materials.component';

describe('CourseReadingMaterialsComponent', () => {
  let component: CourseReadingMaterialsComponent;
  let fixture: ComponentFixture<CourseReadingMaterialsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CourseReadingMaterialsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CourseReadingMaterialsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
