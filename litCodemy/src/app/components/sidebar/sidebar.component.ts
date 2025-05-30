import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth.service';
import { UserService } from '../../service/user.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent {

  
  isNavOpen = false;
  isLoggedIn = false;
  profilePictureUrl: string = '';


  constructor(
      public authService: AuthService,
      public router: Router,
      public userService: UserService,
  ) {} 

  @Input() sidebarOpen: boolean = true;

  dropdownOpen: boolean = true;

  toggleDropdown() {
    this.dropdownOpen = !this.dropdownOpen;
  }


  toggleSidebar() {
    this.sidebarOpen = !this.sidebarOpen;
  }


  isMobile(): boolean {
    return window.innerWidth < 768; // md breakpoint in Tailwind
  }


  ngOnInit(): void {
    this.isLoggedIn = this.authService.isLoggedIn();
    this.userService.getProfilePictureUrl().subscribe(url => {
      this.profilePictureUrl = url;
    });

    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login']);
    }
  }
    

  closeDropdown() {
    setTimeout(() => {
      this.dropdownOpen = false;
    }, 150);
  }


  // Generate random avatar ID for default profile image
  randomUserImageId(): number {
    return Math.floor(Math.random() * 70) + 1; // Random ID between 1 and 70
  }  


  showProfileTabs = false;
  activeTab: string | null = null;

  toggleProfileTabs() {
    this.showProfileTabs = !this.showProfileTabs;
    if (!this.showProfileTabs) {
      this.activeTab = null; // Reset active tab on collapse
    }
  }

  setActiveTab(tab: string) {
    this.activeTab = tab;
  }

  // Dummy user data
  user = {
    name: 'John Doe',
    role: 'Instructor'
  };

  showManagementView = false;

  activeCourses = [
    { name: 'Intro to Programming', progress: 40, totalTopics: 10 },
    { name: 'Database Systems', progress: 75, totalTopics: 12 },
  ];

  completedCourses = [
    { name: 'FSEU 1 Jan 2021 (Uganda)', status: 'Not Graduated Yet.' },
  ];


}
