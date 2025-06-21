import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { CreateCourseComponent } from '../../create-course/create-course.component';
import { CreateTopicComponent } from '../../create-topic/create-topic.component';
import { CreateSubtopicComponent } from '../../create-subtopic/create-subtopic.component';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-management-view-component',
  standalone: true,
  imports: [RouterModule, FormsModule ,CommonModule, CreateCourseComponent, CreateTopicComponent, CreateSubtopicComponent],
  templateUrl: './management-view-component.component.html',
  styleUrl: './management-view-component.component.scss'
})
export class ManagementViewComponentComponent {

  activeTab = 'course';

  setTab(tab: string) {
    this.activeTab = tab;
  }
}
