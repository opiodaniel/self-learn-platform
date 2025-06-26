import { Routes } from '@angular/router';
import { RegisterComponent } from './auth-components/register/register.component';
import { LoginComponent } from './auth-components/login/login.component';
import { MainComponent } from './components/main/main.component';
import { CourseReadingMaterialsComponent } from './components/course-reading-materials/course-reading-materials.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { ManagementViewComponentComponent } from './components/management-view-component/management-view-component.component';
import { UpdatePasswordComponent } from './pages/update-password/update-password.component';
import { UpdateProfileComponent } from './pages/update-profile/update-profile.component';
import { RegisterToCourseComponent } from './components/register-to-course/register-to-course.component';
import { DashboardContentComponent } from './components/dashboard-content/dashboard-content.component';
import { CreateCourseComponent } from './create-course/create-course.component';
import { CreateTopicComponent } from './create-topic/create-topic.component';
import { CreateSubtopicComponent } from './create-subtopic/create-subtopic.component';

export const routes: Routes = [

    
    {path:'register', component:RegisterComponent},
    {path:'login', component:LoginComponent},

    {path:'', component:MainComponent},
    { path: 'reading-material', component: CourseReadingMaterialsComponent },
    {
      path: 'dashboard', 
      component: DashboardComponent, 
      children: [
        { path: 'dashboard-content', component: DashboardContentComponent },
        { path: 'management', component: ManagementViewComponentComponent },
        { path: 'update-profile', component: UpdateProfileComponent },
        { path: 'update-password', component: UpdatePasswordComponent },
      ]
    },
    { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
    { path: 'management/course/:id', component: CourseReadingMaterialsComponent },
    { path: 'register-to-course/:id', component: RegisterToCourseComponent }
    

];
