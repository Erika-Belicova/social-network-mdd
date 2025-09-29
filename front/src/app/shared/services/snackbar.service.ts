import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({ providedIn: 'root' })
// service to show different types of snack-bar notifications
export class SnackbarService {
  constructor(private snackBar: MatSnackBar) {}

  // show error message in snackbar
  showError(message: string, duration = 5000) {
    this.snackBar.open(message, 'Fermer', {
      duration,
      panelClass: ['snackbar-error']
    });
  }

  // show success message in snackbar
  showSuccess(message: string, duration = 5000) {
    this.snackBar.open(message, 'Fermer', {
      duration,
      panelClass: ['snackbar-success']
    });
  }

  // show informational message in snackbar
  showInfo(message: string, duration = 5000) {
    this.snackBar.open(message, 'Fermer', {
      duration,
      panelClass: ['snackbar-info']
    });
  }
}
