import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { CreateCourseComponent } from '../../create-course/create-course.component';
import { CreateTopicComponent } from '../../create-topic/create-topic.component';
import { CreateSubtopicComponent } from '../../create-subtopic/create-subtopic.component';
import { FormsModule } from '@angular/forms';
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

  setTab(tab: string) {
    this.activeTab = tab;
  }

  userCourses: any[] = []; // All user-enrolled courses
  activeCourses: any[] = [];
  completedCourses: any[] = [];

  isAdmin: boolean = false; // You'll replace this with real role logic

  constructor(private courseService: CourseService, private authService: AuthService) {}

  ngOnInit() {
    // this.isAdmin = this.authService.getCurrentUserRole() === 'ADMIN'; // Adjust as needed

    // this.fetchUserCourses();
  }

  // fetchUserCourses() {
  //   const request = {
  //     SERVICE: 'Enrollment',
  //     ACTION: 'getUserCourses',
  //     userId: this.authService.getCurrentUserId()
  //   };

  //   this.courseService.getUserCourses(request).subscribe({
  //     next: (response) => {
  //       this.userCourses = response.returnObject || [];

  //       // Temporary logic — separate into active and completed (based on dummy field)
  //       this.activeCourses = this.userCourses.filter(c => !c.completed);
  //       this.completedCourses = this.userCourses.filter(c => c.completed);
  //     },
  //     error: (err) => console.error('Failed to load user courses', err),
  //   });
  // }
  
}
