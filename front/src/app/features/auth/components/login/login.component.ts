import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router'
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { TokenService } from '../../../../core/services/token.service';
import { SnackbarService } from '../../../../shared/services/snackbar.service';

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
    private snackbarService: SnackbarService
  ) { }

  ngOnInit(): void {
    // initialize reactive form with username or email and password fields
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
    // attach global click listener after view init
    document.addEventListener('click', this.handleClickOutside);
  }

  ngOnDestroy(): void {
    // remove global click listener to prevent memory leaks
    document.removeEventListener('click', this.handleClickOutside);
  }

  login(): void {
    this.submitted = true;

    if (this.loginForm.invalid) {
      return;
    }

    // send login request and handle token and navigation
    this.authService.login(this.loginForm.value).subscribe({
      next: (response) => {
        this.tokenService.saveToken(response.token);
        this.router.navigate(['/posts']);
      },
      error: (err) => {
        const message = err.error?.message || 'Identifiants invalides.';
        this.snackbarService.showError(message);
      }
    });
  }
}