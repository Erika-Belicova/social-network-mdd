import { Component } from '@angular/core';
import { Router } from '@angular/router'

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  constructor(private router: Router) { }

  login() {
    // call auth service to login user

    // then navigate to posts
    this.router.navigate(['/posts']);
  }

}
