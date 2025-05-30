import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

interface CreateTestRequest {
  title: string;
}

interface QuestionDTO {
  content: string;
  multipleAnswersAllowed: boolean;
}

interface AnswerOptionDTO {
  answerText: string;
  correct: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class TestManagementService {
  private baseUrl = 'http://localhost:8000/api/v1/tests';

  constructor(private http: HttpClient) {}

  createTest(topicId: number, title: string): Observable<any> {
    const headers = this.getAuthHeaders(); 
    const body: CreateTestRequest = { title };
    return this.http.post(`${this.baseUrl}/topic/${topicId}`, body, {headers});
  }

  addQuestion(testId: number, question: QuestionDTO): Observable<any> {
    const headers = this.getAuthHeaders(); 
    return this.http.post(`${this.baseUrl}/${testId}/questions`, question, {headers});
  }

  addAnswer(questionId: number, answer: AnswerOptionDTO): Observable<any> {
    const headers = this.getAuthHeaders(); 
    return this.http.post(`${this.baseUrl}/questions/${questionId}/answers`, answer, {headers});
  }

  submitTest(testId: number, userId: number, answers: Record<number, number[]>): Observable<any> {
    const headers = this.getAuthHeaders(); 
    return this.http.post(`${this.baseUrl}/${testId}/submit?userId=${userId}`, answers, {headers});
  }

  getTestQuestions(testId: number): Observable<any> {
    const headers = this.getAuthHeaders(); 
    return this.http.get(`${this.baseUrl}/${testId}/questions`, {headers});
  }

  getAnswersByQuestion(questionId: number): Observable<any> {
    const headers = this.getAuthHeaders(); 
    return this.http.get(`${this.baseUrl}/questions/${questionId}/answers`, {headers});
  }

  getUserSubmissions(userId: number): Observable<any> {
    const headers = this.getAuthHeaders(); 
    return this.http.get(`${this.baseUrl}/submissions/user/${userId}`, {headers});
  }

  getSubmissionsByTest(testId: number): Observable<any> {
    const headers = this.getAuthHeaders(); 
    return this.http.get(`${this.baseUrl}/${testId}/submissions`, {headers});
  }

  getSubmissionDetails(submissionId: number): Observable<any> {
    const headers = this.getAuthHeaders(); 
    return this.http.get(`${this.baseUrl}/submissions/${submissionId}`, {headers});
  }

  getTestByTopic(topicId: number): Observable<any> {
    const headers = this.getAuthHeaders(); 
    return this.http.get(`${this.baseUrl}/by-topic/${topicId}`, {headers});
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
