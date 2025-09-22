import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';

import { TopicDTO } from '../../../topics/interfaces/topic-dto';
import { PostService } from '../../services/post.service';
import { UserService } from '../../../user/services/user.service';
import { PostRequestDTO } from '../../interfaces/post-request-dto';
import { PostResponseDTO } from '../../interfaces/post-response-dto';
import { UserDTO } from 'src/app/features/user/interfaces/user-dto';

@Component({
  selector: 'app-post-form',
  templateUrl: './post-form.component.html',
  styleUrls: ['./post-form.component.scss']
})
export class PostFormComponent implements OnInit {
  topics: TopicDTO[] = [];
  postForm!: FormGroup;

  constructor(
    private router: Router,
    private fb: FormBuilder,
    private postService: PostService,
    private userService: UserService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.fetchSubscribedTopics();

    this.postForm = this.fb.group({
      topicId: ['', Validators.required],
      title: ['', Validators.required],
      content: ['', [Validators.required]]
    });
  }

  fetchSubscribedTopics() {
    this.userService.getCurrentUser().subscribe({
      next: (user: UserDTO) => {
        this.topics = user.topics; // only subscribed topics
      },
      error: (err) => {
        console.error('Error fetching user subscriptions:', err);
        this.snackBar.open(
          'Impossible de charger vos thèmes abonnés.',
          'Fermer',
          { duration: 4000, panelClass: ['snackbar-error'] }
        );
      }
    });
  }

  submitPost() {
    this.postForm.markAllAsTouched();
    if (this.postForm.invalid) {
      return;
    }

    const postData: PostRequestDTO = this.postForm.value;

    this.postService.createPost(postData).subscribe({
      next: (post: PostResponseDTO) => {
        this.snackBar.open(
          'Article créé avec succès !',
          'Fermer',
          { duration: 3000, panelClass: ['snackbar-success'] }
        );
        this.router.navigate(['/posts', post.id]);
      },
      error: (err) => {
        console.error('Error creating post:', err);

        let message = 'Une erreur est survenue lors de la création de l’article.';
        
        // check for duplicate title error from the back-end
        if (err?.status === 409) {
          message = 'Un article avec ce titre existe déjà.';
        }

        this.snackBar.open(message, 'Fermer', { duration: 4000, panelClass: ['snackbar-error'] });
      }
    });
  }
}
