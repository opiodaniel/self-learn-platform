import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard-content',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './dashboard-content.component.html',
  styleUrl: './dashboard-content.component.scss'
})
export class DashboardContentComponent {


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
      isNew: true,
    progress: 10
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
    
  ];

}