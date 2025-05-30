import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CourseService } from '../../service/course.service';
import { VoteService } from '../../service/vote.service';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../service/auth.service';
import { UserService } from '../../service/user.service';

@Component({
  selector: 'app-main',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './main.component.html',
  styleUrl: './main.component.scss'
})
export class MainComponent implements OnInit {

  isLoggedIn = false;
  dropdownOpen = false;
  profilePictureUrl: string = '';

  constructor(
    public authService: AuthService,
    public router: Router,
    public userService: UserService,
  ) {}

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

  courses = [
    {
      icon: 'assets/python.jpeg',
      title: 'Python',
      desc: 'Learn animation techniques to create stunning motion graphics.',

    category: 'Web Dev',
      isNew: true,
    progress: 50
    },
    {
      icon: 'assets/java.jpeg',
      title: 'Java',
      desc: 'Create beautiful, user-friendly interfaces for modern apps.',

    category: 'Web Dev',
      isNew: false,
    progress: 0
    },
    {
      icon: 'assets/angular.jpeg',
      title: 'Angular',
      desc: 'Master lighting, focus, and composition in digital photography.',

    category: 'Web Dev',
      isNew: false,
    progress: 0
    },
    {
      icon: 'assets/react.jpeg',
      title: 'React',
      desc: 'Understand and invest in cryptocurrencies with confidence.',

    category: 'Web Dev',
      isNew: false,
    progress: 0
    },
    {
      icon: 'assets/springboot.png',
      title: 'Business',
      desc: 'Step-by-step guide to building and scaling your business.',

    category: 'Web Dev',
      isNew: false,
    progress: 0
    },
    {
      icon: 'assets/django.png',
      title: 'Django',
      desc: 'Learn to drive traffic and convert leads using digital tools.',

    category: 'Web Dev',
      isNew: false,
    progress: 0
    },
    {
      icon: 'assets/frontend.jpeg',
      title: 'Frontend Development',
      desc: 'Master HTML, CSS, and JavaScript with modern frameworks.',

    category: 'Web Dev',
      isNew: false,
    progress: 0
    },
    {
      icon: 'assets/backend.jpeg',
      title: 'Backend Development',
      desc: 'Build scalable APIs with Node, Spring Boot, and Django.',

    category: 'Web Dev',
      isNew: false,
    progress: 0
    },
    {
      icon: 'assets/icons/mobile.svg',
      title: 'Mobile Apps',
      desc: 'Create responsive mobile apps using Flutter & React Native.',

    category: 'Web Dev',
      isNew: false,
    progress: 0
    },
    {
      icon: 'assets/icons/data.svg',
      title: 'Data Science',
      desc: 'Work with Python, Pandas, and Machine Learning techniques.',

    category: 'Web Dev',
      isNew: false,
    progress: 0
    },
    {
      icon: 'assets/icons/ai.svg',
      title: 'AI & ML',
      desc: 'Dive into Artificial Intelligence with hands-on labs.',

    category: 'Web Dev',
      isNew: false,
    progress: 0
    },
    {
      icon: 'assets/icons/devops.svg',
      title: 'DevOps',
      desc: 'Master CI/CD pipelines, Docker, and Kubernetes.',

    category: 'Web Dev',
      isNew: false,
    progress: 0
    },
  ];

  // Generate random avatar ID for default profile image
  randomUserImageId(): number {
    return Math.floor(Math.random() * 70) + 1; // Random ID between 1 and 70
  }

}
