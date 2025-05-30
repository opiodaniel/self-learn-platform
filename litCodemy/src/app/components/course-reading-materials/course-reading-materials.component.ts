import { CommonModule } from '@angular/common';
import { Component, HostListener } from '@angular/core';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../service/auth.service';
import { UserService } from '../../service/user.service';

@Component({
  selector: 'app-course-reading-materials',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './course-reading-materials.component.html',
  styleUrl: './course-reading-materials.component.scss'
})
export class CourseReadingMaterialsComponent {

  selectedSubsection: { sectionId: number; subsection: any } | null = null;
  isNavOpen = false;
  sidebarOpen = true;
  activeSectionId: number | null = null;
  activeSection: any = null;
  activeSubsection: any = null;

  isLoggedIn = false;
  dropdownOpen = false;
  profilePictureUrl: string = '';


  constructor(
      public authService: AuthService,
      public router: Router,
      public userService: UserService,
  ) {} 


  toggleSidebar() {
    this.sidebarOpen = !this.sidebarOpen;
  }


  isMobile(): boolean {
    return window.innerWidth < 768; // md breakpoint in Tailwind
  }

  // Optional: Handle resize dynamically
  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    if (event.target.innerWidth > 768 && this.sidebarOpen) {
      this.sidebarOpen = true; // Keep open on desktop
    }
  }  

sections = [
  {
    id: 1,
    title: 'Installation',
    expanded: false,
    subsections: [
      { id: 1, title: 'Intro to Setup', content: 'This is intro setup content...' },
      { id: 2, title: 'Install Python', content: 'How to install Python on Windows...' }
    ]
  },
  {
    id: 2,
    title: 'Configuration',
    expanded: false,
    subsections: [
      { id: 1, title: 'Environment Variables', content: 'Set environment variables...' },
      { id: 2, title: 'Database Setup', content: 'Configure PostgreSQL...' }
    ]
  },
  {
    id: 3,
    title: 'Setup',
    expanded: false,
    subsections: [
      { id: 1, title: 'Environment Variables', content: 'Set environment variables...' },
      { id: 2, title: 'Database Setup', content: 'Configure PostgreSQL...' }
    ]
  }
];


  selectSubsection(sectionId: number, sub: any) {
    this.selectedSubsection = { sectionId, subsection: sub };
  }  

  get displayedContent() {
    return this.selectedSubsection?.subsection?.content || 'Select a subsection from the sidebar to view details.';
  }  

  // Toggle Section/Main-Tabs
  toggleSection(id: number) {
    const section = this.sections.find(s => s.id === id);
  
    if (section) {
      const isCurrentlyExpanded = section.expanded;
  
      // Collapse all sections first
      this.sections.forEach(s => s.expanded = false);
  
      // If the clicked section was not expanded, expand it
      if (!isCurrentlyExpanded) {
        section.expanded = true;
        this.activeSectionId = id;
      } else {
        // If it was already expanded, collapse it and clear activeSectionId
        this.activeSectionId = null;
      }
    }
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
    

  toggleDropdown() {
    this.dropdownOpen = !this.dropdownOpen;
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

}
