import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CourseService } from '../../service/course.service';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-register-to-course',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './register-to-course.component.html',
  styleUrl: './register-to-course.component.scss'
})
export class RegisterToCourseComponent implements OnInit {

  showToast = false;
  courseId!: number;
  course: any;
  loading = true;
  isEnrolled: boolean = false; // default


  constructor(
    private route: ActivatedRoute, 
    private courseService: CourseService, 
    public authService: AuthService,
    public router: Router,
  ) {}

  registerCourse() {
    
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login']);
    }
  
    this.courseService.enrollToCourse(this.courseId).subscribe({
      next: (res) => {
        this.showToast = true;
        setTimeout(() => {
          this.showToast = false;
          this.checkIfUserIsEnrolled(this.courseId);
        }, 3000);
      },
      error: (err) => {
        alert('Failed to enroll: ' + (err.error?.message || 'Unknown error'));
        console.error('Enrollment error:', err);
      }
    });
  }  

  ngOnInit(): void {
    this.courseId = +this.route.snapshot.paramMap.get('id')!;    
    this.loadCourseDetails();
    this.checkIfUserIsEnrolled(this.courseId);
  }

  loadCourseDetails() {
    this.courseService.getCourseById(this.courseId).subscribe({
      next: (data) => {
        this.course = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Failed to load course', err);
        this.loading = false;
      }
    });
  }
  
  checkIfUserIsEnrolled(courseId: number) {
    this.courseService.checkEnrollment(courseId).subscribe({
      next: (res) => {
        this.isEnrolled = res?.alreadyEnrolled === true;
      },
      error: (err) => {
        console.warn('Could not check enrollment:', err);
      }
    });
  }  

}
