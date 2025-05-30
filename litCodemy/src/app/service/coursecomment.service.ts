import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CourseCommentService {
  private baseUrl = 'http://localhost:8000/api/v1/comment';

  constructor(private http: HttpClient) {}

  getComments(courseId: number): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.get(`${this.baseUrl}/${courseId}/comments`, {headers});
  }

  addComment(courseId: number, content: string): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.post(`${this.baseUrl}/on/course/${courseId}`, { content }, {headers});
  }

  deleteComment(commentId: number): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.delete(`${this.baseUrl}/delete/${commentId}`, {headers});
  }

  updateComment(commentId: number, body: { content: string }): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.put(`${this.baseUrl}/update/${commentId}`, body, {headers});
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

