import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { TokenService } from '../../../../core/services/token.service';
import { RegisterRequestDTO } from '../../interfaces/register-request-dto';
import { SnackbarService } from '../../../../shared/services/snackbar.service';

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

  // map back-end error messages to translations
  private readonly translatedMessages: Record<string, string> = {
    'Username is already in use': "Ce nom d’utilisateur est déjà utilisé.",
    'Email is already in use': "Cet e-mail est déjà utilisé."
  };

  constructor(
    private router: Router,
    private fb: FormBuilder,
    private authService: AuthService,
    private tokenService: TokenService,
    private snackbarService: SnackbarService
  ) { }

  ngOnInit(): void {
    // initialize reactive form with validation rules
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
    // attach global click listener after view initialization
    document.addEventListener('click', this.handleClickOutside);
  }

  ngOnDestroy(): void {
    // remove listener to prevent memory leaks
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

    // send registration request and handle response including error translation
    this.authService.register(registerRequest).subscribe({
      next: (response) => {
        this.tokenService.saveToken(response.token);
        this.router.navigate(['/posts']);
      },
      error: (err) => {
        const backendMessage: string = err.error?.error || '';
        const message = this.translatedMessages[backendMessage] || 'Une erreur inconnue est survenue.';
        this.snackbarService.showError(message);
        this.serverError = message;
        this.onError = true;
      }
    });
  }
}
