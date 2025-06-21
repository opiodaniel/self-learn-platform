import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CourseService } from '../service/course.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-create-course',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './create-course.component.html',
  styleUrl: './create-course.component.scss'
})
export class CreateCourseComponent {
  title: string = '';
  description: string = '';
  categoryType: string = '';
  image: File | null = null;

  categories = ["LANGUAGE", "FRAMEWORK", "DATABASE", "DEVOPS", "LIBRARY"]; // these must match your Java Enum


  constructor(private courseService: CourseService) {}

  onFileChange(event: any) {
    const file = event.target.files?.[0];
    if (file) {
      this.image = file;
    }
  }

  onSubmit() {
    const formData = new FormData();
    formData.append('title', this.title);
    formData.append('description', this.description);
    formData.append('categoryType', this.categoryType);
    if (this.image) {
      formData.append('image', this.image);
    }

    this.courseService.createCourse(formData).subscribe({
      next: () => {
        alert('✅ Course created successfully!');
        this.resetForm();
      },
      error: (err) => {
        console.error('❌ Error creating course:', err);
        alert('Failed to create course: ' + (err.error?.message || 'Unknown error'));
      }
    });
  }

  resetForm() {
    this.title = '';
    this.description = '';
    this.categoryType = '';
    this.image = null;
  }
}
