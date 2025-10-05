import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { TokenService } from '../services/token.service';

export const authGuard: CanActivateFn = (route, state) => {
  const tokenService = inject(TokenService);
  const router = inject(Router);

  const token = tokenService.getToken();

  if (token) {
    return true; // allow route activation if token exists
  } else {
    return router.createUrlTree(['/login']); // redirect to login if token is missing
  }
};
