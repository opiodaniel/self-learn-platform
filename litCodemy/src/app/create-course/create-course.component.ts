import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CourseService } from '../service/course.service';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-create-course',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterLink],
  templateUrl: './create-course.component.html',
  styleUrl: './create-course.component.scss'
})
export class CreateCourseComponent implements OnInit  {
  title: string = '';
  description: string = '';
  categoryType: string = '';
  image: File | null = null;

  courses: any[] = [];

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


// Publish Course

  publishCourse(courseId: number) {
    this.courseService.publishCourse(courseId).subscribe({
      next: (res) => {
        alert('Course published successfully');
        this.loadCourses(); // refresh list after publishing
      },
      error: (err) => {
        console.error('Failed to publish course', err);
        alert('Failed to publish course: ' + (err.error?.message || 'Unknown error'));
      }
    });
  }
  

  // Getting all courses.

  ngOnInit() {
    this.loadCourses();
  }

  loadCourses() {
    this.courseService.getAllCourses().subscribe({
      next: (data) => (console.log(data), this.courses = data),
      error: (err) => {
        console.error('Failed to load courses', err),
        alert('Failed to load courses: ' + (err.error?.message || 'Unknown error'));
      }
    });
  }


  // Pagination variables
currentPage: number = 1;
pageSize: number = 4; // Courses per page

get paginatedCourses(): any[] {
  const start = (this.currentPage - 1) * this.pageSize;
  return this.courses.slice(start, start + this.pageSize);
}

get totalPages(): number {
  return Math.ceil(this.courses.length / this.pageSize);
}

goToPage(page: number) {
  this.currentPage = page;
}


}
