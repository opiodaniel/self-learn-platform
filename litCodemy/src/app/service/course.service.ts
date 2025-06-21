import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { map, Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class CourseService {
  private baseUrl = 'http://localhost:8000/api/course/create'; 
  private apiUrl = 'http://localhost:8000/api/v1/';

  constructor(private http: HttpClient) {}

  createCourse(courseData: FormData): Observable<any> {
    const headers = this.getAuthHeaders(); // DO NOT set Content-Type here
    return this.http.post(`${this.baseUrl}`, courseData, { headers });
  }  

  publishCourse(courseId: number): Observable<any> {
    const body = {
      SERVICE: 'Course',
      ACTION: 'publishCourse',
      courseId: courseId
    };
  
    return this.http.post<any>(this.apiUrl, body);
  }

  getAllCourses(): Observable<any[]> {
    const requestBody = {
      SERVICE: 'Course',
      ACTION: 'getAllCourses'
    };
    return this.http.post<any>(this.apiUrl, requestBody).pipe(
      map((response) => response.returnObject || [])
    );
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

