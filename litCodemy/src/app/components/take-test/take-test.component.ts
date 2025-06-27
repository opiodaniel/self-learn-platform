import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { CourseService } from '../../service/course.service';
import { TopicService } from '../../service/topic.service';

@Component({
  selector: 'app-take-test',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './take-test.component.html',
  styleUrl: './take-test.component.scss'
})
export class TakeTestComponent implements OnInit {
  topicId!: number;
  test: any;
  answersForm!: FormGroup;
  submitting = false;
  result: any = null;

  constructor(
    private route: ActivatedRoute,
    private courseService: CourseService,
    private topicService: TopicService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.topicId = +this.route.snapshot.paramMap.get('topicId')!;
    this.loadTest();
  }

  loadTest() {
    this.topicService.getTestByTopicId(this.topicId).subscribe({
      next: (test) => {
        this.test = test;
        const group: any = {};
        test.questions.forEach((q: any) => {
          group[q.id] = [null]; // Initially no answer selected
        });
        this.answersForm = this.fb.group(group);
      },
      error: () => {
        alert('Failed to load test.');
      }
    });
  }

  submit() {
    if (!this.answersForm.valid) return;

    this.submitting = true;
    const submittedAnswers = Object.keys(this.answersForm.value).map(qId => ({
      questionId: +qId,
      selectedAnswerId: this.answersForm.value[qId]
    }));

    this.topicService.submitTest({
      testId: this.test.id,
      answers: submittedAnswers
    }).subscribe({
      next: (res) => {
        this.result = res;
        this.submitting = false;
      },
      error: () => {
        alert('Failed to submit test.');
        this.submitting = false;
      }
    });
  }
}
