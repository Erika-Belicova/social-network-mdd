import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PostService } from '../../services/post.service';
import { PostResponseDTO } from '../../interfaces/post-response-dto';
import { CommentService } from '../../services/comment.service';
import { TopicService } from '../../../topics/services/topic.service';

@Component({
  selector: 'app-post-detail',
  templateUrl: './post-detail.component.html',
  styleUrls: ['./post-detail.component.scss']
})
export class PostDetailComponent implements OnInit {
  post!: PostResponseDTO;  // will hold post data
  commentForm!: FormGroup;
  topicTitle: string = '';

  constructor(
    private route: ActivatedRoute,
    private postService: PostService,
    private commentService: CommentService,
    private topicService: TopicService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    // build the form
    this.commentForm = this.fb.group({
      comment: ['', Validators.required]
    });

    const postId = Number(this.route.snapshot.paramMap.get('id'));
    this.postService.getPostById(postId).subscribe({
      next: (data) => {
        this.post = data;
        if (!this.post.comments) this.post.comments = [];
      },
      error: (err) => console.error('Error fetching post:', err)
    });
  }

  addComment() {
    if (this.commentForm.invalid) {
      this.commentForm.markAllAsTouched();
      return;
    }

    const payload = { content: this.commentForm.value.comment };

    this.commentService.addComment(this.post.id, payload).subscribe({
      next: () => {
        // re-fetch the post so comments update
        this.postService.getPostById(this.post.id).subscribe({
          next: (data) => {
            this.post = data;
          },
          error: (err) => console.error('Error fetching post after adding comment:', err)
        });

        // clear the input
        this.commentForm.reset();
      },
      error: (err) => console.error('Error adding comment:', err)
    });
  }
}
