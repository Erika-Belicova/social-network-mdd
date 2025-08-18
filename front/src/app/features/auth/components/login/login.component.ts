import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router'
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { TokenService } from '../../../../core/services/token.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild('loginFormRef') loginFormRef!: ElementRef;

  loginForm!: FormGroup;
  submitted = false;

  constructor(
    private router: Router,
    private fb: FormBuilder,
    private authService: AuthService,
    private tokenService: TokenService,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      usernameOrEmail: ['', [Validators.required]],
      password: ['', [Validators.required]],
    });
  }

  // reset submitted flag if clicking outside the form
  private handleClickOutside = (event: MouseEvent) => {
    if (
      this.loginFormRef &&
      !this.loginFormRef.nativeElement.contains(event.target)
    ) {
      this.submitted = false;
    }
  };

  ngAfterViewInit(): void {
    document.addEventListener('click', this.handleClickOutside);
  }

  ngOnDestroy(): void {
    document.removeEventListener('click', this.handleClickOutside);
  }

  login(): void {
    this.submitted = true;

    if (this.loginForm.invalid) {
      return;
    }

    this.authService.login(this.loginForm.value).subscribe({
      next: (response) => {
        this.tokenService.saveToken(response.token);
        this.router.navigate(['/posts']);
      },
      error: (err) => {
        const message = err.error?.message || 'Identifiants invalides.';
        this.snackBar.open(message, 'Fermer', {
          duration: 5000
        });
      }
    });
  }
}