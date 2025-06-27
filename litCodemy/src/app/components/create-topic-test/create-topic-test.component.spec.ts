import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateTopicTestComponent } from './create-topic-test.component';

describe('CreateTopicTestComponent', () => {
  let component: CreateTopicTestComponent;
  let fixture: ComponentFixture<CreateTopicTestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateTopicTestComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CreateTopicTestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
