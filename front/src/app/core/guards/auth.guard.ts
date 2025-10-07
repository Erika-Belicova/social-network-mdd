import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { TokenService } from '../services/token.service';
import { UserService } from '../../features/user/services/user.service';
import { catchError, map, of } from 'rxjs';

export const authGuard: CanActivateFn = (route, state) => {
  const tokenService = inject(TokenService);
  const userService = inject(UserService);
  const router = inject(Router);

  const token = tokenService.getToken();

  if (!token) {
    return router.createUrlTree(['/login']); // navigate to login if no token
  }

  // verify token validity
  return userService.getCurrentUser().pipe(
    map(() => true), // allow route activation if token is valid
    catchError(() => of(router.createUrlTree(['/login']))) // navigate to login if not
  );
};
