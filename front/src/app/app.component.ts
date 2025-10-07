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
  title = 'front'; 
  currentRoute = ''; // store current route path

  // capture the drawer from template
  @ViewChild('drawer') drawer!: MatSidenav;

  constructor(private router: Router, private authService: AuthService) {
    // subscribe to route changes to update currentRoute
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.currentRoute = event.urlAfterRedirects;
      }
    })
  }

  // check if the current page is the homepage
  isHomepage(): boolean {
    return this.currentRoute === '/';
  }

  // check if the current page is a public route
  isPublicRoute(): boolean {
    return ['/login', '/register', '/404'].includes(this.currentRoute);
  }

  // logout user and redirect to homepage
  logout() {
    this.authService.logout();
    this.router.navigate(['/']);
  }
}
