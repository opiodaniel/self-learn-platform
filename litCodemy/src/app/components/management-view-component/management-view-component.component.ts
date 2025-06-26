import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { CreateCourseComponent } from '../../create-course/create-course.component';
import { CreateTopicComponent } from '../../create-topic/create-topic.component';
import { CreateSubtopicComponent } from '../../create-subtopic/create-subtopic.component';
import { FormBuilder, FormGroup, FormsModule } from '@angular/forms';
import { AuthService } from '../../service/auth.service';
import { CourseService } from '../../service/course.service';

@Component({
  selector: 'app-management-view-component',
  standalone: true,
  imports: [RouterModule, FormsModule ,CommonModule, CreateCourseComponent, CreateTopicComponent, CreateSubtopicComponent],
  templateUrl: './management-view-component.component.html',
  styleUrl: './management-view-component.component.scss'
})
export class ManagementViewComponentComponent  implements OnInit {

  activeTab = 'course';
  courses: any[] = [];

  userRole: string | null = null;

  setTab(tab: string) {
    this.activeTab = tab;
  }

  constructor(private courseService: CourseService, private authService: AuthService) {}


  ngOnInit(): void {
    this.userRole = this.authService.getCurrentUserRole();
    this.getUserEnrolledCourses()
  }

  isAdmin(): boolean {
    return this.userRole === 'Administrator';
  }

  isUser(): boolean {
    return this.userRole === 'Programmer'; // or adapt based on your system
  }


  getUserEnrolledCourses() {
    this.courseService.getUserEnrolledCourses().subscribe({
      next: (data) => {
        this.courses = data;
        console.log(this.courses)
      },
      error: (err) => {
        console.error('Failed to load courses', err),
        alert('Failed to load courses: ' + (err.error?.message || 'Unknown error'));
      }
    });
  }

  
}
