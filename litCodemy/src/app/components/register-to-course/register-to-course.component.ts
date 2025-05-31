import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-register-to-course',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './register-to-course.component.html',
  styleUrl: './register-to-course.component.scss'
})
export class RegisterToCourseComponent {
  showToast = false;

  constructor(private router: Router) {}

  goBack() {
    this.router.navigate(['/courses']); // adjust path as needed
  }

  registerCourse() {
    this.showToast = true;
    setTimeout(() => {
      this.showToast = false;
    }, 3000); // hide after 3 seconds
  }
}
