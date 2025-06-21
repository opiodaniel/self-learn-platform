import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class CourseService {
  private baseUrl = 'http://localhost:8000/api/course/create'; 

  constructor(private http: HttpClient) {}

  createCourse(courseData: FormData): Observable<any> {
    const headers = this.getAuthHeaders(); // DO NOT set Content-Type here
    return this.http.post(`${this.baseUrl}`, courseData, { headers });
  }
  
  

  getAllCourses(): Observable<any> {
    //const headers = this.getAuthHeaders();
    return this.http.get(`${this.baseUrl}`);
  }

  getCourseById(courseId: number): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.get(`${this.baseUrl}/${courseId}`, { headers });
  }


  private getAuthHeaders(): HttpHeaders {
    //console.log("localStorage: ", localStorage)
    const token = localStorage.getItem('authToken'); 
    //console.log("TOKEN: ", token)
    return new HttpHeaders({
      //'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
  }
}

