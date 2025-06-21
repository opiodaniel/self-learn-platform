import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CourseService } from '../../service/course.service';
import { VoteService } from '../../service/vote.service';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../service/auth.service';
import { UserService } from '../../service/user.service';
import { Course, CourseResponseDTO } from '../../model/course-response-dto';
import { MessageService } from '../../service/message.service';

@Component({
  selector: 'app-main',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './main.component.html',
  styleUrl: './main.component.scss'
})
export class MainComponent implements OnInit {

  courses: any[] = [];

  isLoggedIn = false;
  dropdownOpen = false;
  profilePictureUrl: string = '';
  welcomeMessage: string | null = null;

  loading: boolean = false;

  constructor(
    public authService: AuthService,
    public router: Router,
    public userService: UserService,
    private messageService: MessageService,
    private courseService: CourseService
  ) {}

  ngOnInit(): void {
    this.isLoggedIn = this.authService.isLoggedIn();
    this.userService.getProfilePictureUrl().subscribe(url => {
      this.profilePictureUrl = url;
    });

    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login']);
    }

    this.messageService.message$.subscribe(msg => {
      this.welcomeMessage = msg;
      
      // Optional: clear message after a few seconds
      if (msg) {
        setTimeout(() => this.messageService.clearMessage(), 5000);
      }
    });

    this.loadCourses();

  }
    

    toggleDropdown() {
      this.dropdownOpen = !this.dropdownOpen;
    }

    closeDropdown() {
      setTimeout(() => {
        this.dropdownOpen = false;
      }, 150);
    }


  // Getting all courses.

  loadCourses() {
    this.loading = true;
    this.courseService.getAllCourses().subscribe({
      next: (data) => {
        //console.log(data)
        this.courses = (data || []).filter((course: any) => course.published === true);
        this.loading = false;
      },
      error: (err) => {
        console.error('Failed to load courses', err),
        alert('Failed to load courses: ' + (err.error?.message || 'Unknown error'));
      }
    });
  }

  // Pagination variables
  currentPage: number = 1;
  pageSize: number = 8; // Courses per page

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

  // Generate random avatar ID for default profile image
  randomUserImageId(): number {
    return Math.floor(Math.random() * 70) + 1; // Random ID between 1 and 70
  }

}
