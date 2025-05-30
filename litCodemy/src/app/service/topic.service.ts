import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class TopicService {
  private baseUrl = 'http://localhost:8000/api/v1/topics';

  constructor(private http: HttpClient) {}

  createTopic(courseId: number, topicData: any): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.post(`${this.baseUrl}/course/${courseId}`, topicData, {headers});
  }

  getTopicsByCourse(courseId: number): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.get(`${this.baseUrl}/course/${courseId}`, {headers});
  }

  getTopicById(topicId: number): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.get(`${this.baseUrl}/${topicId}`, {headers});
  }

  updateTopic(topicId: number, topicData: { title: string; description: string }): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.put(`${this.baseUrl}/update/${topicId}`, topicData, {headers});
  }

  deleteTopic(topicId: number): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.delete(`${this.baseUrl}/delete/${topicId}`, {headers});
  }

  getAllTopics(): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.get<any>(`${this.baseUrl}`, {headers});
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
