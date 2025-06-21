import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';
import { Observable } from 'rxjs';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl = 'http://localhost:8000/api/v1/';
  private tokenKey = 'authToken';
  private tokenExpiryTimeout: any;

  constructor(private http: HttpClient, private router: Router, public userService: UserService,) {}

  /** Call this from LoginComponent */
  // loginUser(credentials: any): Observable<any> {
  //   return this.http.post<any>(`${this.baseUrl}/login`, credentials)
  // }

  loginUser(credentials: { email: string; password: string }): Observable<any> {
    const payload = {
      SERVICE: 'Auth',
      ACTION: 'login',
      data: credentials
    };
    return this.http.post<any>(`${this.baseUrl}`, payload);
  }
  

  register(userData: any): Observable<any> {
    const payload = {
      SERVICE: 'Auth',
      ACTION: 'register',
      data: userData
    }
    return this.http.post<any>(`${this.baseUrl}`, payload);
  }

  login(token: string) {
    localStorage.setItem(this.tokenKey, token);
  }

  logout() {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem('profilePictureUrl');
    this.userService.setCurrentUserId(null);
    this.clearTokenWatcher()
    this.router.navigate(['/']);
  }
  

  isLoggedIn(): boolean {
    this.startTokenWatcher();
    return !!localStorage.getItem(this.tokenKey);
  }

  getCurrentUser(): { username: string, role: string, imageUrl?: string } | null {
    const token = localStorage.getItem(this.tokenKey);
    if (!token) return null;
  
    try {
      const decoded: any = jwtDecode(token);
      return {
        username: decoded.sub || decoded.username || '',
        role: decoded.role || '',
        imageUrl: decoded.imageUrl || ''
      };
    } catch (error) {
      console.error('Invalid token:', error);
      return null;
    }
  }
  
  

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }


  startTokenWatcher() {
    const token = this.getToken();
    if (token) {
      const payload = JSON.parse(atob(token.split('.')[1])); //header.payload.signature // atob()
      const expiryTime = payload.exp * 1000; // payload.exp token's expiry time in seconds // converted to milliseconds
      const currentTime = Date.now();  // Get the current time in milliseconds.
      const timeUntilExpiry = expiryTime - currentTime;   // Calculate how much time is left before the token expires.

      if (timeUntilExpiry <= 0) {
        this.logout(); // If token already expired → immediately log out the user.
      } else {
        this.tokenExpiryTimeout = setTimeout(() => {
          this.logout();   //set a timeout to log out the user when it eventually expires.
        }, timeUntilExpiry);
      }
    }
  }

  clearTokenWatcher() {
    if (this.tokenExpiryTimeout) {
      clearTimeout(this.tokenExpiryTimeout);
    }
  }

    //  decode the token to get the username // npm install jwt-decode
  getCurrentUsername(): string | null {
    const token = localStorage.getItem('token');
    if (!token) return null;

    try {
      const decoded: any = jwtDecode(token);
      return decoded.sub || decoded.username || null; // Adjust based on your token structure
    } catch (error) {
      console.error('Invalid token:', error);
      return null;
    }
  }

  getCurrentUserId(): number | null {
    const token = localStorage.getItem('token');
    if (!token) return null;
  
    const decoded: any = jwtDecode(token);
    return decoded?.userId || null;
  }

}

// export class AuthService {

//   private tokenKey = 'authToken';

//   constructor() { }

//   private http = inject(HttpClient);
//   private apiUrl = 'http://localhost:8000/api/v1/auth'; 

//   register(user: any): Observable<any> {
//     return this.http.post(`${this.apiUrl}/register`, user);
//   }

//   login(user: any): Observable<any> {
//     return this.http.post(`${this.apiUrl}/login`, user);
//   }


//   //  decode the token to get the username // npm install jwt-decode
//   getCurrentUsername(): string | null {
//     const token = localStorage.getItem('token');
//     if (!token) return null;

//     try {
//       const decoded: any = jwtDecode(token);
//       return decoded.sub || decoded.username || null; // Adjust based on your token structure
//     } catch (error) {
//       console.error('Invalid token:', error);
//       return null;
//     }
//   }

//   getCurrentUserId(): number | null {
//     const token = localStorage.getItem('token');
//     if (!token) return null;
  
//     const decoded: any = jwtDecode(token);
//     return decoded?.userId || null;
//   }

// }
