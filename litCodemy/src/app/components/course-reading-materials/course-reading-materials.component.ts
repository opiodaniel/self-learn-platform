import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, HostListener, Input } from '@angular/core';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../service/auth.service';
import { UserService } from '../../service/user.service';
import { CourseService } from '../../service/course.service';
import { ActivatedRoute } from '@angular/router';
import { CreateTopicComponent } from '../../create-topic/create-topic.component';
import { CreateSubtopicComponent } from '../../create-subtopic/create-subtopic.component';
import html from 'editorjs-html';


@Component({
  selector: 'app-course-reading-materials',
  standalone: true,
  imports: [CommonModule, RouterModule, CreateTopicComponent, CreateSubtopicComponent],
  templateUrl: './course-reading-materials.component.html',
  styleUrl: './course-reading-materials.component.scss'
})
export class CourseReadingMaterialsComponent {

  //npm install editorjs-html
  // npm install -D @tailwindcss/typography

  selectedSubsection: { sectionId: number; subsection: any } | null = null;
  isNavOpen = false;
  sidebarOpen = true;
  activeSectionId: number | null = null;
  activeSection: any = null;
  activeSubsection: any = null;
  course: any;

  sections: any = null;

  isLoggedIn = false;
  dropdownOpen = false;
  profilePictureUrl: string = '';
  userRole: string | null = null;


  isTopicModalOpen = false;
  isSubtopicModalOpen = false;
  selectedTopicId: number | null = null;


  constructor(
      public authService: AuthService,
      public router: Router,
      private route: ActivatedRoute,
      public userService: UserService,
      public courseService: CourseService,
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


loadCourseDetails(id: number) {
  this.courseService.getCourseById(id).subscribe(course => {
    this.course = course;
    //console.log("Course", this.course)
    this.loadTopics(course.id);
  });
}

loadTopics(courseId: number) {
  this.courseService.getTopicsByCourseId(courseId).subscribe((topics: any[]) => {
    //console.log("Topics", topics)
    this.sections = topics.map((topic: any) => ({
      id: topic.id,
      title: topic.title,
      expanded: false,
      subsections: topic.subtopics || []
    }));
  });
}

  selectSubsection(sectionId: number, sub: any) {
    this.selectedSubsection = { sectionId, subsection: sub };
  }  

  get renderedContent(): string {
    try {
      const content = this.selectedSubsection?.subsection?.content;
      if (!content) {
        console.warn('No content found');
        return 'No content found.';
      }
  
      //console.log('Raw Content:', content); // 🔍 Log raw JSON string
  
      let parsedData;
      try {
        parsedData = JSON.parse(content);
      } catch (e) {
        console.error('Failed to parse JSON:', e);
        return 'Invalid content format.';
      }
  
      //console.log('Parsed Data:', parsedData); // 🔍 Log parsed object
  
      const htmlParser = html();
      const htmlOutput = htmlParser.parse(parsedData);

      //console.log('htmlOutput Data:', htmlOutput); // 🔍 Log parsed object
  
      return htmlOutput;
  
    } catch (e) {
      console.error('Failed to render content:', e);
      return 'Error rendering content.';
    }
  }  

  // Toggle Section/Main-Tabs
  toggleSection(id: number) {
    const section = this.sections.find((s: { id: number; }) => s.id === id);
  
    if (section) {
      const isCurrentlyExpanded = section.expanded;
  
      // Collapse all sections first
      this.sections.forEach((s: { expanded: boolean; }) => s.expanded = false);
  
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

    const courseId = this.route.snapshot.paramMap.get('id');
    if (courseId) {
      this.loadCourseDetails(+courseId);
    }

    this.userRole = this.authService.getCurrentUserRole();
  } 

  toggleDropdown() {
    this.dropdownOpen = !this.dropdownOpen;
  }

  closeDropdown() {
    setTimeout(() => {
      this.dropdownOpen = false;
    }, 150);
  }


  isAdmin(): boolean {
    return this.userRole === 'Administrator';
  }
  

  // --------TOPICS--------------

    openTopicModal() {
      this.isTopicModalOpen = true;
    }

    closeTopicModal() {
      this.isTopicModalOpen = false;      
      const courseId = this.route.snapshot.paramMap.get('id');
      if (courseId) {
        this.loadCourseDetails(+courseId);
      }
    }

    onTopicCreated() {
      console.log("onTopicCreated")
      this.closeTopicModal();
      this.sections();
    }



  // ----------SUBTOPICS-------------  

  openSubtopicModal(topicId: number) {
    this.selectedTopicId = topicId;
    this.isSubtopicModalOpen = true;
  }

  closeSubtopicModal() {
    this.isSubtopicModalOpen = false;
    const courseId = this.route.snapshot.paramMap.get('id');
    if (courseId) {
      this.loadCourseDetails(+courseId);
    }
  }

  onSubtopicCreated(event: any) {
    this.closeSubtopicModal();
  }


  startTest(topicId: number): void {
    // 🔸 For now, show mock data
    console.log(`Start test for topic: ${topicId}`);
    
    // 🔸 In future, navigate to a test-taking component
    this.router.navigate(['/take-test', topicId]);
  }
  

}
