import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { TokenService } from '../services/token.service';
import { AuthService } from '../../features/auth/services/auth.service';
import { SnackbarService } from '../../shared/services/snackbar.service';
import { Router } from '@angular/router';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(
    private tokenService: TokenService, 
    private authService: AuthService, 
    private snackbarService: SnackbarService,
    private router: Router
  ) { }

  intercept<T>(req: HttpRequest<T>, next: HttpHandler): Observable<HttpEvent<T>> {
    const token = this.tokenService.getToken();

    const excludedEndpoints = ['/api/auth/login', '/api/auth/register'];
    const url = new URL(req.url, window.location.origin);

    let request = req;
    // attach Authorization header if token exists and endpoint is not excluded
    if (token && !excludedEndpoints.includes(url.pathname)) {
      request = req.clone({
        headers: req.headers.set('Authorization', `Bearer ${token}`)
      });
    }

    return next.handle(request).pipe(
      // handle 401 errors globally by cleaning up data and redirecting to login
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401 && !excludedEndpoints.includes(url.pathname)) {
          setTimeout(() => { // timeout needed to show snackbar error
            this.snackbarService.showError('Session expirée. Veuillez vous reconnecter.');
          }, 0);
          this.authService.logout(); // remove token, clear cached user, reset localStorage
          this.router.navigate(['/login']);
        }
        return throwError(() => error);
      })
    );
  }
}
