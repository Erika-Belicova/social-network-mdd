import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { TokenService } from '../../services/token.service';
import { RegisterRequestDTO } from '../../interfaces/register-request-dto';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  registerForm!: FormGroup;
  public onError = false;

  constructor(
    private router: Router,
    private fb: FormBuilder,
    private authService: AuthService,
    private tokenService: TokenService
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

  public register(): void {
    this.onError = false;

    if (this.registerForm.invalid) {
      return;
    }

    const registerRequest: RegisterRequestDTO = this.registerForm.value;

    this.authService.register(registerRequest).subscribe({
      next: (response) => {
        this.tokenService.saveToken(response.token);
        this.router.navigate(['/posts']);
      },
      error: () => this.onError = true,
    });
  }
}
