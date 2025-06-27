import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TopicService } from '../../service/topic.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-create-topic-test',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './create-topic-test.component.html',
  styleUrl: './create-topic-test.component.scss'
})
export class CreateTopicTestComponent {

  @Input() topicId!: number;
  @Output() submitted = new EventEmitter<void>();

  testTitle = '';
  questions: any[] = [];
  currentIndex = 0;
  message = '';

  constructor(private topicService: TopicService, private route: ActivatedRoute) {}

  ngOnInit() {
    this.addQuestion(); // Start with one question
  }

  addQuestion() {
    this.questions.push({
      content: '',
      answerOptions: [
        { answerText: '', isCorrect: false },
        { answerText: '', isCorrect: false }
      ]
    });
    this.currentIndex = this.questions.length - 1; // Show newly added question
  }

  removeAnswerOption(qIndex: number, optIndex: number) {
    this.questions[qIndex].answerOptions.splice(optIndex, 1);
  }

  addAnswerOption(qIndex: number) {
    this.questions[qIndex].answerOptions.push({ answerText: '', isCorrect: false });
  }

  nextQuestion() {
    if (this.currentIndex < this.questions.length - 1) this.currentIndex++;
  }

  prevQuestion() {
    if (this.currentIndex > 0) this.currentIndex--;
  }

  submitTest() {
    const data = {
      title: this.testTitle,
      topicId: this.topicId,
      questions: this.questions
    };

    this.topicService.createTopicTest(data).subscribe({
      next: () => {
        this.message = '✅ Test created!';
        setTimeout(() => {
          this.submitted.emit(); // Notify parent to close modal
        }, 1500);
      },
      error: () => {
        this.message = '❌ Failed to create test.';
      }
    });
  }
}