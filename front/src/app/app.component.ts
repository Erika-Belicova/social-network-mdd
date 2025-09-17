import { Component, ViewChild } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { MatSidenav } from '@angular/material/sidenav';
import { AuthService } from 'src/app/features/auth/services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  currentRoute = '';

  // capture the drawer from template
  @ViewChild('drawer') drawer!: MatSidenav;

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
