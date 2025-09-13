import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
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
  newComment: string = '';
  topicTitle: string = '';

  constructor(
    private route: ActivatedRoute,
    private postService: PostService,
    private commentService: CommentService,
    private topicService: TopicService
  ) {}

  ngOnInit(): void {
    const postId = Number(this.route.snapshot.paramMap.get('id'));
    this.postService.getPostById(postId).subscribe({
      next: (data) => {
        this.post = data;
        if (!this.post.comments) this.post.comments = [];

        // fetch topic title
        this.topicService.getTopics().subscribe({
          next: (topics) => {
            const topic = topics.find(topic => topic.id === this.post.topic_id);
            this.topicTitle = topic ? topic.title : 'Topic unknown';
          },
          error: (err) => console.error('Error fetching topics:', err)
        });
      },
      error: (err) => console.error('Error fetching post:', err)
    });
  }

  addComment() {
    if (!this.newComment.trim()) return;

    const payload = { content: this.newComment };

    this.commentService.addComment(this.post.id, payload).subscribe({
      next: () => {
        // re-fetch the post so post.comments are updated
        this.postService.getPostById(this.post.id).subscribe({
          next: (data) => {
            this.post = data; // template will re-render
          },
          error: (err) => console.error('Error fetching post after adding comment:', err)
        });

        // clear the input
        this.newComment = '';
      },
      error: (err) => console.error('Error adding comment:', err)
    });
  }
}
