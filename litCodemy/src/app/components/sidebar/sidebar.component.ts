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
  @Input() sidebarOpen: boolean = true;

  isNavOpen = false;
  isLoggedIn = false;
  dropdownOpen: boolean = true;

  profilePictureUrl: string = '';
  userName: string = '';
  userRole: string = '';

  constructor(
    public authService: AuthService,
    public router: Router,
    public userService: UserService
  ) {}


  ngOnInit(): void {
    this.isLoggedIn = this.authService.isLoggedIn();

    if (!this.isLoggedIn) {
      this.router.navigate(['/login']);
      return;
    }

    const user = this.authService.getCurrentUser();
    if (user) {
      this.userName = user.username;
      this.userRole = user.role;
      //this.profilePictureUrl = user.imageUrl || this.getDefaultProfilePicture();
      this.userService.getProfilePictureUrl().subscribe(url => {
        this.profilePictureUrl = url;
      });
    }
  }

  toggleDropdown() {
    this.dropdownOpen = !this.dropdownOpen;
  }



  // getDefaultProfilePicture(): string {
  //   const id = Math.floor(Math.random() * 70) + 1;
  //   return `https://i.pravatar.cc/150?img=${id}`;
  // }

  logout() {
    this.authService.logout();
  }
}
