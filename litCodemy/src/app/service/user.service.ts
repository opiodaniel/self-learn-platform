import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
//import { User } from '../models/post';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private currentUserIdSubject = new BehaviorSubject<number | null>(null);
  public currentUserId$ = this.currentUserIdSubject.asObservable();
  public defaultAvatar: string = 'https://www.w3schools.com/howto/img_avatar.png';
  private profilePictureUrl: BehaviorSubject<string>;
  private apiUrl = 'http://localhost:8000/api/v1/profile';
  private apiAuthUrl = 'http://localhost:8000/api/v1/auth';
  private topContributorsUrl = 'http://localhost:8000/api/v1/user/top-contributors';
  

  constructor(private http: HttpClient) {
    const savedUrl = localStorage.getItem('profilePictureUrl');
    this.profilePictureUrl = new BehaviorSubject<string>(savedUrl || this.defaultAvatar);
  }



  setCurrentUserId(id: number | null) {
    this.currentUserIdSubject.next(id);
  }

  get currentUserId(): number | null {
    return this.currentUserIdSubject.value;
  }


  getUserProfile(): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.get<any>(`${this.apiUrl}`, { headers });
  }

  updateProfilePicture(formData: FormData): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.post<any>(`${this.apiUrl}/upload_profile_pic`, formData, { headers });
  }


  getCurrentUser(): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.get<any>(`${this.apiUrl}/current_user`, { headers });
  }

  getProfilePictureUrl(): Observable<string> {
    return this.profilePictureUrl.asObservable();
  }

  setProfilePictureUrl(url: string | null | undefined): void {
    const finalUrl = url ? url : this.defaultAvatar;
    localStorage.setItem('profilePictureUrl', finalUrl);
    this.profilePictureUrl.next(finalUrl);
  }

  deleteAccount(): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.delete(`${this.apiAuthUrl}/delete/account`, { headers });
  }


  // Get top contributors
  // getTopContributors(): Observable<User[]> {
  //   return this.http.get<User[]>(this.topContributorsUrl);
  // }

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('authToken');
    return new HttpHeaders({
      //'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
  }
 
}

