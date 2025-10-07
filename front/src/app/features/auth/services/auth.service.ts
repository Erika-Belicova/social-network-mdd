import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { RegisterRequestDTO } from '../interfaces/register-request-dto';
import { AuthResponse } from '../interfaces/auth-response';
import { LoginRequestDTO } from '../interfaces/login-request-dto';
import { TokenService } from '../../../core/services/token.service';
import { UserService } from '../../../features/user/services/user.service';

@Injectable({ providedIn: 'root' })
export class AuthService {
	private baseUrl = '/api/auth';

	constructor(
    private http: HttpClient, 
    private tokenService: TokenService,
    private userService: UserService
  ) { }

	public register(registerRequest: RegisterRequestDTO): Observable<AuthResponse> {
		return this.http.post<AuthResponse>(`${this.baseUrl}/register`, registerRequest)
		  .pipe(
        tap(response => {
          // save token after successful registration
          this.tokenService.saveToken(response.token);
          this.userService.clearCache(); // reset cached user data
        })
      );
	}

	public login(loginRequest: LoginRequestDTO): Observable<AuthResponse> {
		return this.http.post<AuthResponse>(`${this.baseUrl}/login`, loginRequest)
		  .pipe(
        tap(response => {
          // save token after successful login
          this.tokenService.saveToken(response.token);
          this.userService.clearCache(); // reset cached user data
        })
      );
	}

	public logout(): void {
    this.tokenService.removeToken();
    this.userService.clearCache(); // reset cached user data
    localStorage.removeItem('postsSortOrder'); // reset sort order on logout
  }
}
