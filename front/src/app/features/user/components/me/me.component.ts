import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../../auth/services/auth.service';
import { TopicService } from '../../../topics/services/topic.service';
import { TopicDTO } from '../../../topics/interfaces/topic-dto';
import { UserDTO } from '../../interfaces/user-dto';
import { UpdateUserDTO } from '../../interfaces/update-user-dto';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';

// optional password validator: only checks pattern if not empty
function optionalPasswordValidator(control: AbstractControl): ValidationErrors | null {
  const value = control.value;
  if (!value) return null; // skip validation if empty
  const pattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;
  return pattern.test(value) ? null : { pattern: true };
}

@Component({
  selector: 'app-me',
  templateUrl: './me.component.html',
  styleUrls: ['./me.component.scss']
})
export class MeComponent implements OnInit {
  profileForm!: FormGroup;
  currentUser!: UserDTO;
  userSubscriptions: TopicDTO[] = [];
  subscribedTopicIds = new Set<number>();

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private topicService: TopicService,
    private authService: AuthService,
    private snackBar: MatSnackBar,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadUserProfile();
  }

  loadUserProfile() {
    this.userService.getCurrentUser().subscribe({
      next: (user: UserDTO) => {
        this.currentUser = user;
        this.userSubscriptions = user.topics;
        this.subscribedTopicIds = new Set(user.topics.map(topic => topic.id));

        this.profileForm = this.fb.group({
          username: [user.username, Validators.required],
          email: [user.email, [Validators.required, Validators.email]],
          password: ['', optionalPasswordValidator]
        });
      },
      error: (err) => console.error('Error loading user profile:', err)
    });
  }
  
  updateUser() {
    this.profileForm.markAllAsTouched();
    if (!this.profileForm.valid) return;

    // check if profile information have been changed
    const formValues = this.profileForm.value;
    const hasChanges =
      formValues.username !== this.currentUser.username ||
      formValues.email !== this.currentUser.email ||
      (!!formValues.password && formValues.password.trim() !== '');

    if (!hasChanges) {
      this.snackBar.open(
        "Aucune modification détectée.",
        'Fermer',
        { duration: 3000, panelClass: ['snackbar-info'] }
      );
      return;
    }

    // build DTO with mandatory fields from form, password optional
    const updatedData: Partial<UpdateUserDTO> = {
      username: this.profileForm.value.username || this.currentUser.username,
      email: this.profileForm.value.email || this.currentUser.email,
      name: this.currentUser.username
    };

    // only include password if the user typed something
    if (this.profileForm.value.password) {
      updatedData.password = this.profileForm.value.password;
    }

    this.userService.updateUser(updatedData as UpdateUserDTO).subscribe({
      next: () => {
        // show snackbar
        this.snackBar.open(
          'Identifiants modifiés avec succès. Veuillez vous reconnecter avec vos nouveaux identifiants.',
          'Fermer',
          { duration: 5000 }
        );

        // logout user and redirect to login page
        this.authService.logout();
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error('Error updating user:', err);

        let message = 'Une erreur est survenue lors de la mise à jour.'; // default message

        if (err.status === 409) {
          message = "Le nom d'utilisateur ou l'adresse e-mail est déjà utilisé.";
        } else if (err.status === 400) {
          message = "Données invalides. Veuillez vérifier le formulaire.";
        }

        this.snackBar.open(message, 'Fermer', { duration: 5000, panelClass: ['snackbar-error'] });

        // reset the form to current user data so invalid input is discarded
        this.loadUserProfile();
      }
    });
  }

  unsubscribe(topicId: number) {
    this.userService.unsubscribe(topicId).subscribe({
      next: () => {
        this.userSubscriptions = this.userSubscriptions.filter(topic => topic.id !== topicId);
        this.subscribedTopicIds.delete(topicId);
      },
      error: (err) => {
        console.error('Error unsubscribing:', err);
        const message = err?.error?.message || 'Une erreur est survenue lors du désabonnement.';
        this.snackBar.open(message, 'Fermer', { duration: 5000, panelClass: ['snackbar-error'] });
      }
    });
  }
}
