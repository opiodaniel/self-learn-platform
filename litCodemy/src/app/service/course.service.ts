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


  enrollToCourse(courseId: number): Observable<any> {
    const headers = this.getAuthHeaders();
    const payload = {
      SERVICE: 'Enrollment',
      ACTION: 'enrollToCourse',
      data: { courseId }
    };
  
    return this.http.post<any>(this.apiUrl, payload, { headers });
  }

  checkEnrollment(courseId: number): Observable<{ alreadyEnrolled: boolean }> {
    const headers = this.getAuthHeaders();
    return this.http.post<any>(this.apiUrl, {
      SERVICE: 'Enrollment',
      ACTION: 'checkEnrollment',
      data: { courseId }
    }, { headers }).pipe(
      map(response => ({
        alreadyEnrolled: response.returnCode === 200 && response.returnObject?.alreadyEnrolled === true
      }))
    );
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

  getCourseById(id: number): Observable<any> {
    return this.http.post<any>(this.apiUrl, {
      SERVICE: 'Course',
      ACTION: 'getCourseById',
      data: { courseId: id }
    }).pipe(
      map(response => response.returnObject) 
    );
  }

  getUserEnrolledCourses(): Observable<any[]> {
    const headers = this.getAuthHeaders();
    return this.http.post<any>(this.apiUrl, {
      SERVICE: 'Enrollment',
      ACTION: 'getUserEnrolledCourses'
    }, { headers }).pipe(
      map(response => response.returnObject) 
    );
  }
  

  createTopic(data: any): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.post<any>(this.apiUrl, {
      SERVICE: 'Topic',
      ACTION: 'createTopic',
      data: data
    }, { headers }).pipe(map(res => res.returnObject));
  }

  getTopicsByCourseId(id: number): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.post<any>(this.apiUrl, {
      SERVICE: 'Topic',
      ACTION: 'getCourseTopics',
      data: { courseId: id }
    }, { headers }).pipe(
      map(response => response.returnObject) 
    );
  }

  createSubTopic(data: any): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.post<any>(this.apiUrl, {
      SERVICE: 'SubTopic',
      ACTION: 'createTopicSubTopic',
      data: data
    }, { headers }).pipe(map(res => res.returnObject));
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

