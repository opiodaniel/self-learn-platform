import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CourseService } from '../service/course.service';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-create-topic',
  standalone: true,
  imports: [CommonModule,  FormsModule, ReactiveFormsModule],
  templateUrl: './create-topic.component.html',
  styleUrl: './create-topic.component.scss'
})
export class CreateTopicComponent implements OnInit {

  topicForm!: FormGroup;
  courses: any[] = [];
  successMessage = '';
  errorMessage = '';

  constructor(private fb: FormBuilder, private courseService: CourseService,private route: ActivatedRoute) {}


  ngOnInit(): void {
    const courseId = this.route.snapshot.paramMap.get('id');
    if (!courseId) {
      this.errorMessage = 'Course ID is missing in the URL.';
      return;
    }
  
    this.topicForm = this.fb.group({
      courseId: [+courseId], // Automatically fill it
      title: ['', Validators.required],
      description: ['', Validators.required],
    });

    this.courseService.getAllCourses().subscribe({
      next: (data) => {
        this.courses = data.filter(c => c.published); // only show published courses
      },
      error: () => {
        this.errorMessage = 'Failed to load courses.';
      }
    });
  }
  

  onSubmit(): void {
    if (this.topicForm.invalid) return;

    this.courseService.createTopic(this.topicForm.value).subscribe({
      next: (res) => {
        this.successMessage = '✅ Topic created successfully!';
        this.topicForm.reset();
        setTimeout(() => (this.successMessage = ''), 3000);
      },
      error: (err) => {
        this.errorMessage = err.error?.message || '❌ Failed to create topic.';
        setTimeout(() => (this.errorMessage = ''), 3000);
      }
    });
  }

}
