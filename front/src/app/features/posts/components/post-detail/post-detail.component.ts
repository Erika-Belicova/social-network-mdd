import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PostService } from '../../services/post.service';
import { PostResponseDTO } from '../../interfaces/post-response-dto';
import { CommentService } from '../../services/comment.service';
import { SnackbarService } from '../../../../shared/services/snackbar.service';

@Component({
  selector: 'app-post-detail',
  templateUrl: './post-detail.component.html',
  styleUrls: ['./post-detail.component.scss']
})
export class PostDetailComponent implements OnInit {
  post!: PostResponseDTO;  // holds post data including comments
  commentForm!: FormGroup;
  topicTitle: string = '';
  @ViewChild('commentInput') commentInput!: ElementRef<HTMLTextAreaElement>; // used to scroll to comment input

  constructor(
    private route: ActivatedRoute,
    private postService: PostService,
    private commentService: CommentService,
    private fb: FormBuilder,
    private snackbarService: SnackbarService
  ) {}

  ngOnInit(): void {
    // initialize reactive comment form
    this.commentForm = this.fb.group({
      comment: ['', Validators.required]
    });

    // fetch post by ID from route param
    const postId = Number(this.route.snapshot.paramMap.get('id'));
    this.postService.getPostById(postId).subscribe({
      next: (data) => {
        this.post = data;
        if (!this.post.comments) this.post.comments = []; // ensure comments array exists
      },
      error: () => {
        this.snackbarService.showError('Impossible de charger l’article.');
      }
    });
  }

  addComment() {
    if (this.commentForm.invalid) {
      this.commentForm.markAllAsTouched(); // mark fields to show validation errors
      return;
    }

    const payload = { content: this.commentForm.value.comment };

    this.commentService.addComment(this.post.id, payload).subscribe({
      next: () => {
        // refresh post to update comments list
        this.postService.getPostById(this.post.id).subscribe({
          next: (data) => {
            this.post = data;
          },
          error: () => this.snackbarService.showError(
            "Erreur lors de la récupération de l’article après l’ajout du commentaire."
          )
        });

        // clear the input
        this.commentForm.reset();

        // scroll to comment input smoothly after adding
        setTimeout(() => {
          this.commentInput.nativeElement.scrollIntoView({ behavior: 'smooth', block: 'center' });
        }, 100);
      },
      error: () => {
        this.snackbarService.showError('Impossible d’ajouter le commentaire.');
      }
    });
  }
}
