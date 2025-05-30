import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private baseUrl = 'http://localhost:8000/api/v1/categories'; 
  constructor(private http: HttpClient) {}

  getCategories(): Observable<any[]> {
    const headers = this.getAuthHeaders();
    return this.http.get<any[]>(`${this.baseUrl}`, {headers});
  }

  createCategory(data: { name: string }) {
    const headers = this.getAuthHeaders();
    return this.http.post(`${this.baseUrl}`, data, {headers});
  }
  
  updateCategory(id: number, data: { name: string }) {
    const headers = this.getAuthHeaders();
    return this.http.put(`${this.baseUrl}/update/${id}`, data, {headers});
  }
  
  deleteCategory(id: number) {
    const headers = this.getAuthHeaders();
    return this.http.delete(`${this.baseUrl}/delete/${id}`, {headers});
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
