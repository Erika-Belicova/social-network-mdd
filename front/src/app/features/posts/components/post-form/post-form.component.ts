import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
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
  selectedTopic: number | null = null;

  title = '';
  content = '';

  constructor(
    private router: Router,
    private postService: PostService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.fetchSubscribedTopics();
  }

  fetchSubscribedTopics() {
    this.userService.getCurrentUser().subscribe({
      next: (user: UserDTO) => {
        this.topics = user.topics; // only the user's subscribed topics
      },
      error: (err) => console.error('Error fetching user subscriptions:', err)
    });
  }

  submitPost() {
    if (!this.selectedTopic) {
      alert('Veuillez sélectionner un thème.');
      return;
    }

    if (!this.title || !this.content) {
      alert('Veuillez remplir le titre et le contenu.');
      return;
    }

    const postData: PostRequestDTO = {
      topicId: this.selectedTopic,
      title: this.title,
      content: this.content
    };

    this.postService.createPost(postData).subscribe({
      next: (post: PostResponseDTO) => {
        // navigate to the newly created post
        this.router.navigate(['/posts', post.id]);
      },
      error: (err) => {
        console.error('Error creating post:', err);
        alert('Une erreur est survenue lors de la création de l’article.');
      }
    });
  }
}
