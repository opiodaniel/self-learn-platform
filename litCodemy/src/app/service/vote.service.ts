import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class VoteService {
  private baseUrl = 'http://localhost:8000/api/v1/vote';

  constructor(private http: HttpClient) {}

  voteCourse(courseId: number, voteType: 'UPVOTE' | 'DOWNVOTE') {
    const headers = this.getAuthHeaders();
    return this.http.post(`${this.baseUrl}/course/${courseId}?voteType=${voteType}`, {}, {headers});
  }

  getVoteCounts(courseId: number) {
    const headers = this.getAuthHeaders();
    return this.http.get<any>(`${this.baseUrl}/${courseId}/vote-counts`, {headers});
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
