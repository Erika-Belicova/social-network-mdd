import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { UserDTO } from '../interfaces/user-dto';
import { UpdateUserDTO } from '../interfaces/update-user-dto';
import { catchError, shareReplay, tap } from 'rxjs/operators';
import { TokenService } from '../../../core/services/token.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private cachedUser$?: Observable<UserDTO>;

  constructor(
    private http: HttpClient,
    private tokenService: TokenService
  ) {}

  // fetch current user
  getCurrentUser(): Observable<UserDTO> {
    if (!this.tokenService.getToken()) {
      return throwError(() => new Error('No token found.')); // throw if no token
    }

    // if user data is not cached, fetch from API
    if (!this.cachedUser$) {
      this.cachedUser$ = this.http.get<UserDTO>('/api/auth/me').pipe(
        shareReplay(1), // cache the result
        catchError(err => {
          if (err.status === 401) {
            // if 401, remove token and clear cache
            this.tokenService.removeToken();
            this.clearCache();
          }
          return throwError(() => err);
        })
      );
    }

    return this.cachedUser$;
  }

  // clear the cached current user manually
  clearCache() {
    this.cachedUser$ = undefined;
  }

  getUserById(userId: number): Observable<UserDTO> {
    return this.http.get<UserDTO>(`/api/auth/${userId}`);
  }

  // update current user profile
  updateUser(data: UpdateUserDTO): Observable<void> {
    return this.http.put<void>('/api/auth/me', data).pipe(
      tap(() => this.clearCache()) // refresh user data
    );
  }

  // unsubscribe from a topic
  unsubscribe(topicId: number): Observable<void> {
    return this.http.delete<void>(`/api/topics/${topicId}/subscriptions`).pipe(
      tap(() => this.clearCache()) // refresh user data
    );
  }

  // subscribe to a topic
  subscribe(topicId: number): Observable<void> {
    return this.http.post<void>(`/api/topics/${topicId}/subscriptions`, {}).pipe(
      tap(() => this.clearCache()) // refresh user data
    );
  }
}