import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserDTO } from '../interfaces/user-dto';
import { UpdateUserDTO } from '../interfaces/update-user-dto';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private http: HttpClient) {}

  // fetch current user
  getCurrentUser(): Observable<UserDTO> {
    return this.http.get<UserDTO>('/api/auth/me');
  }

  getUserById(userId: number): Observable<UserDTO> {
    return this.http.get<UserDTO>(`/api/auth/${userId}`);
  }

  // update current user profile
  updateUser(data: UpdateUserDTO): Observable<void> {
    return this.http.put<void>('/api/auth/me', data);
  }

  // unsubscribe from a topic
  unsubscribe(topicId: number): Observable<void> {
    return this.http.delete<void>(`/api/topics/${topicId}/subscriptions`);
  }

  // subscribe to a topic
  subscribe(topicId: number): Observable<void> {
    return this.http.post<void>(`/api/topics/${topicId}/subscriptions`, {});
  }
}