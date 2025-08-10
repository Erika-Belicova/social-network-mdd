import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RegisterRequestDTO } from '../interfaces/register-request-dto';
import { AuthResponse } from '../interfaces/auth-response';
import { environment } from 'src/environments/environment';
import { LoginRequestDTO } from '../interfaces/login-request-dto';

@Injectable({ providedIn: 'root' })
export class AuthService {
	private baseUrl = `${environment.apiUrl}/auth`;

	constructor(private http: HttpClient) { }

	public register(registerRequest: RegisterRequestDTO): Observable<AuthResponse> {
		return this.http.post<AuthResponse>(`${this.baseUrl}/register`, registerRequest);
	}

	public login(loginRequest: LoginRequestDTO): Observable<AuthResponse> {
		return this.http.post<AuthResponse>(`${this.baseUrl}/login`, loginRequest);
	}
}
