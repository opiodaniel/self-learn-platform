// import { Component } from '@angular/core';

// @Component({
//   selector: 'app-login',
//   standalone: true,
//   imports: [],
//   templateUrl: './login.component.html',
//   styleUrl: './login.component.scss'
// })
// export class LoginComponent {

// }

import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule, FormGroup } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../service/auth.service';
import { UserService } from '../../service/user.service';
import { MessageService } from '../../service/message.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit {

  errorMessage: string = '';
  loginForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: AuthService,
    private userService: UserService,
    private messageService: MessageService,
  ) {}

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      email: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  login(): void {

    if (this.loginForm.invalid) {
      this.errorMessage = 'Please enter valid credentials.';
      return;
    }

    const credentials = this.loginForm.value;

    // this.authService.loginUser(credentials).subscribe({
    //   next: res => {
    //     const token = res?.result?.token;
    //     console.log(res)
    //     if (token) {
    //       localStorage.setItem('authToken', token);
    //         this.userService.getCurrentUser().subscribe(user => {
    //         this.userService.setProfilePictureUrl(user.profilePicture);
    //       });
    //       this.router.navigate(['/']); // Navigate after storing token
    //     } else {
    //       this.errorMessage = 'Sorry, wrong credentials. Please try again.';
    //     }
    //   },
    //   error: err => {
    //     console.error(err);
    //     this.errorMessage = 'Sorry, wrong credentials. Please try again.';
    //   }
    // });

  this.authService.loginUser(credentials)
    .subscribe({
      next: (response) => {
        console.log(response)
        var userToken = response.returnObject.access_token;
        if (userToken) {
          this.authService.login(userToken);
          this.messageService.setMessage(response.returnMessage);
          this.router.navigate(['/']);
        } else {
          console.error('No token in response');
        }
      },
      error: (err) => {
        console.error('Login failed', err);
      }
    });

  }
}
