import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SnackbarService } from '../../../../shared/services/snackbar.service';

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
    private snackbarService: SnackbarService
  ) {}

  ngOnInit(): void {
    this.fetchSubscribedTopics(); // fetch subscribed topics of current user

    // initialize reactive form with validation
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
      error: () => {
        this.snackbarService.showError('Impossible de charger vos thèmes abonnés.');
      }
    });
  }

  submitPost() {
    this.postForm.markAllAsTouched(); // trigger validation messages
    if (this.postForm.invalid) {
      return;
    }

    const postData: PostRequestDTO = this.postForm.value;

    // create new post and handle response
    this.postService.createPost(postData).subscribe({
      next: (post: PostResponseDTO) => {
        this.snackbarService.showSuccess('Article créé avec succès !');
        this.router.navigate(['/posts', post.id]); // navigate to created post
      },
      error: (err) => {
        let message = 'Une erreur est survenue lors de la création de l’article.';
        
        // check for duplicate title error from the back-end
        if (err?.status === 409) {
          message = 'Un article avec ce titre existe déjà.';
        }

        this.snackbarService.showError(message);
      }
    });
  }
}
