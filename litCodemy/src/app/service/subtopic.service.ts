import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class SubtopicService {
  private baseUrl = 'http://localhost:8000/api/v1/subtopics';

  constructor(private http: HttpClient) {}

  createSubtopic(topicId: number, subtopicData: any): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.post(`${this.baseUrl}/topic/${topicId}`, subtopicData, {headers});
  }

  getSubtopicsByTopic(topicId: number): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.get(`${this.baseUrl}/topic/${topicId}`, {headers});
  }

  getSubtopicById(subtopicId: number): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.get(`${this.baseUrl}/${subtopicId}`, {headers});
  }

  updateSubtopic(subtopicId: number, subtopicData: any): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.put(`${this.baseUrl}/update/${subtopicId}`, subtopicData, {headers});
  }

  deleteSubtopic(subtopicId: number): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.delete(`${this.baseUrl}/delete/${subtopicId}`, {headers});
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
