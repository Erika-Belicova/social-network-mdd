import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PostService } from '../../services/post.service';
import { PostResponseDTO } from '../../interfaces/post-response-dto';
import { SnackbarService } from '../../../../shared/services/snackbar.service';
import { UserService } from '../../../user/services/user.service';
import { UserDTO } from 'src/app/features/user/interfaces/user-dto';
import { TopicDTO } from '../../../topics/interfaces/topic-dto';

@Component({
  selector: 'app-post-list',
  templateUrl: './post-list.component.html',
  styleUrls: ['./post-list.component.scss']
})
export class PostListComponent implements OnInit {
  posts: (PostResponseDTO & { username?: string })[] = [];
  sortDescending: boolean = true;
  private readonly storageKey = 'postsSortOrder';
  userTopics: TopicDTO[] = []; // store user topics here

  constructor(
    private router: Router, 
    private postService: PostService,
    private userService: UserService,
    private snackbarService: SnackbarService
  ) {}

  ngOnInit(): void {
    // load last sort preference from localStorage
    const storedSort = localStorage.getItem(this.storageKey);
    if (storedSort !== null) {
      this.sortDescending = JSON.parse(storedSort);
    }

    // fetch posts and current user's subscribed topics
    this.fetchPosts();
    this.fetchUserTopics();
  }

  fetchPosts() {
    // fetch all posts and sort them
    this.postService.getPosts().subscribe({
      next: (data) => {
        this.posts = this.sortPosts(data, this.sortDescending);
      },
      error: () => {
        this.snackbarService.showError('Impossible de charger les articles.');
      }
    });
  }

  fetchUserTopics() {
    // fetch current user's subscribed topics
    this.userService.getCurrentUser().subscribe({
      next: (user: UserDTO) => {
        this.userTopics = user.topics || [];
      },
      error: () => {
        this.snackbarService.showError('Impossible de récupérer vos thèmes abonnés.');
      }
    });
  }

  goToPost(postId: number) {
    // navigate to a specific post
    this.router.navigate(['/posts', postId]);
  }

  goToCreatePost() {
    // prevent creating post if user has no subscribed topics
    if (this.userTopics.length === 0) {
      this.snackbarService.showError('Abonnez-vous à un thème pour pouvoir créer un article.');
      return;
    }

    this.router.navigate(['/posts/create']);
  }

  orderBy() {
    this.sortDescending = !this.sortDescending;
    this.posts = this.sortPosts(this.posts, this.sortDescending);
    // persist user's sort preference during the session
    localStorage.setItem(this.storageKey, JSON.stringify(this.sortDescending));
  }

  private sortPosts(posts: PostResponseDTO[], descending: boolean): PostResponseDTO[] {
    // sort posts by creation date, descending or ascending
    return posts.sort((a, b) => {
      const date1 = a.created_at ? new Date(a.created_at).getTime() : 0;
      const date2 = b.created_at ? new Date(b.created_at).getTime() : 0;
      return descending ? date2 - date1 : date1 - date2;
    });
  }
}