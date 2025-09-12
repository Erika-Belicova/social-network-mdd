import { Component } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { AuthService } from 'src/app/features/auth/services/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {
  currentRoute = '';

  constructor(private router: Router, private authService: AuthService) {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.currentRoute = event.urlAfterRedirects;
      }
    })
  }

  isHomepage(): boolean {
    return this.currentRoute === '/';
  }

  isPublicRoute(): boolean {
    return ['/login', '/register'].includes(this.currentRoute);
  }

  logout() {
    this.authService.logout();

    // route to homepage
    this.router.navigate(['/']);
  }
}
