import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManagementViewComponentComponent } from './management-view-component.component';

describe('ManagementViewComponentComponent', () => {
  let component: ManagementViewComponentComponent;
  let fixture: ComponentFixture<ManagementViewComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManagementViewComponentComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ManagementViewComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
