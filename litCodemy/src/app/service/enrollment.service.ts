import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class EnrollmentService {
  private baseUrl = 'http://localhost:8000/api/v1/enrollments';

  constructor(private http: HttpClient) {}

  // Enroll current student into a course
  // enroll(courseId: number): Observable<any> {
  //   const headers = this.getAuthHeaders();
  //   return this.http.post(`${this.baseUrl}`, { courseId }, {headers});
  // }
  
  enroll(userId: number, courseId: number): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.post(`${this.baseUrl}/${userId}/enroll/${courseId}`, {}, { headers });
  }
  

  // Fetch courses the current student is enrolled in
  getMyEnrollments(): Observable<any> {
    const headers = this.getAuthHeaders()
    return this.http.get(`${this.baseUrl}/me`, {headers});
  }

  private getAuthHeaders(): HttpHeaders {
    //console.log("localStorage: ", localStorage)
    const token = localStorage.getItem('token'); 
    //console.log("TOKEN: ", token)
    return new HttpHeaders({
      //'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
  }
}
