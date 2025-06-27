import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { map, Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class TopicService {
  private baseUrl = 'http://localhost:8000/api/v1/';

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


  // createTopicTest(payload: any): Observable<any> {
  //   return this.http.post(`${this.baseUrl}`, payload); // Adjust endpoint as needed
  // }

  createTopicTest(data: any): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.post<any>(this.baseUrl, {
      SERVICE: 'Test',
      ACTION: 'createTopicTest',
      data: data
    }, { headers }).pipe(map(res => res.returnObject));
  }
  


  getTestByTopicId(topicId: number): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.post<any>(this.baseUrl, {
      SERVICE: 'Test',
      ACTION: 'createTopicTest'
    }, { headers }).pipe(map(res => res.returnObject));

  }


  getTestAttempt(data: any) {
    const headers = this.getAuthHeaders();
    return this.http.post<any>(this.baseUrl, {
      SERVICE: 'Test',
      ACTION: 'getTestAttempt',
      data: data
    }, { headers }).pipe(map(res => res.returnObject));
  }
  
  getQuestionsByTestId(data: any) {
    const headers = this.getAuthHeaders();
    return this.http.post<any>(this.baseUrl, {
      SERVICE: 'Test',
      ACTION: 'getQuestionByTestId',
      data: data
    }, { headers }).pipe(map(res => res.returnObject));
  }
  
  submitTest(data: any) {
    const headers = this.getAuthHeaders();
    return this.http.post<any>(this.baseUrl, {
      SERVICE: 'SubmitTest',
      ACTION: 'submitTest',
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
