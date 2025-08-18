import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { TokenService } from '../../../../core/services/token.service';
import { RegisterRequestDTO } from '../../interfaces/register-request-dto';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild('registerFormRef') registerFormRef!: ElementRef;

  registerForm!: FormGroup;
  public onError = false;
  public submitted = false;
  public serverError: string | null = null;

  constructor(
    private router: Router,
    private fb: FormBuilder,
    private authService: AuthService,
    private tokenService: TokenService,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [
        Validators.required,
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/)
      ]],
    });
  }

  // reset submitted flag if clicking outside the form
  private handleClickOutside = (event: MouseEvent) => {
    if (
      this.registerFormRef &&
      !this.registerFormRef.nativeElement.contains(event.target)
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

  public register(): void {
    this.submitted = true;
    this.onError = false;
    this.serverError = null;

    if (this.registerForm.invalid) {
      return;
    }

    const registerRequest: RegisterRequestDTO = this.registerForm.value;

    this.authService.register(registerRequest).subscribe({
      next: (response) => {
        this.tokenService.saveToken(response.token);
        this.router.navigate(['/posts']);
      },
      error: (err) => {
        // Show snackbar with error message
        const message = err.error?.message || 'Une erreur inconnue est survenue.';
        this.snackBar.open(message, 'Fermer', {
          duration: 5000
        });
      }
    });
  }
}
