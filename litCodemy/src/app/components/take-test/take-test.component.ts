import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { CourseService } from '../../service/course.service';
import { TopicService } from '../../service/topic.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-take-test',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './take-test.component.html',
  styleUrl: './take-test.component.scss'
})
export class TakeTestComponent implements OnInit {
  @Input() topicId!: number;
  @Output() submitted = new EventEmitter<void>();

  testId!: number;
  testTitle: string = '';
  questions: any[] = [];
  currentQuestionIndex = 0;
  answers: any[] = [];
  message = '';
  loading = true;

  constructor(private topicService: TopicService) {}

  ngOnInit() {
    this.loadTest();
  }

  loadTest() {
    const data = {
      topicId: this.topicId 
    };
    this.topicService.getTestAttempt(data).subscribe({
      next: (res) => {
        this.testId = res.testId;
        this.testTitle = res.testTitle;
        this.loadQuestions();
      },
      error: () => {
        this.message = '❌ Test not found for this topic.';
        this.loading = false;
      }
    });
  }

  loadQuestions() {
    const data = {
      testId: this.testId 
    };
    this.topicService.getQuestionsByTestId(data).subscribe({
      next: (res) => {
        this.questions = res;
        this.loading = false;
      },
      error: () => {
        this.message = '❌ Failed to load questions.';
        this.loading = false;
      }
    });
  }

  selectAnswer(questionId: number, answerId: number) {
    const existing = this.answers.find(a => a.questionId === questionId);
    if (existing) {
      existing.selectedAnswerId = answerId;
    } else {
      this.answers.push({ questionId, selectedAnswerId: answerId });
    }
  }

  nextQuestion() {
    if (this.currentQuestionIndex < this.questions.length - 1) {
      this.currentQuestionIndex++;
    }
  }

  prevQuestion() {
    if (this.currentQuestionIndex > 0) {
      this.currentQuestionIndex--;
    }
  }

  submit() {
    const data = {
      testId: this.testId,
      answers: this.answers
    };
  
    this.topicService.submitTest(data).subscribe({
      next: (res) => {
        const score = res.score;
        if (score >= 50) {
          this.message = `✅ Test submitted. Score: ${score}%. Great job!`;
          setTimeout(() => this.submitted.emit(), 2500);
        } else {
          this.message = `❌ Score: ${score}%. Please re-do the test to proceed to the next topic.`;
          setTimeout(() => this.submitted.emit(), 2500);
        }
      },
      error: () => {
        this.message = '❌ Submission failed.';
      }
    });
  }
  
}
