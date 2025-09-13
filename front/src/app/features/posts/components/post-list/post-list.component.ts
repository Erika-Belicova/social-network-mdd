import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PostService } from '../../services/post.service';
import { PostResponseDTO } from '../../interfaces/post-response-dto';

@Component({
  selector: 'app-post-list',
  templateUrl: './post-list.component.html',
  styleUrls: ['./post-list.component.scss']
})
export class PostListComponent implements OnInit {
  posts: (PostResponseDTO & { username?: string })[] = [];
  sortDescending: boolean = true;
  private readonly storageKey = 'postsSortOrder';

  constructor(private router: Router, private postService: PostService) {}

  ngOnInit(): void {
    // load last sort preference from localStorage
    const storedSort = localStorage.getItem(this.storageKey);
    if (storedSort !== null) {
      this.sortDescending = JSON.parse(storedSort);
    }

    this.fetchPosts();
  }

  fetchPosts() {
    this.postService.getPosts().subscribe({
      next: (data) => {
        this.posts = this.sortPosts(data, this.sortDescending);
      },
      error: (err) => console.error('Error fetching posts:', err)
    });
  }

  goToPost(postId: number) {
    this.router.navigate(['/posts', postId]);
  }

  orderBy() {
    this.sortDescending = !this.sortDescending;
    this.posts = this.sortPosts(this.posts, this.sortDescending);
    localStorage.setItem(this.storageKey, JSON.stringify(this.sortDescending));
  }

  private sortPosts(posts: PostResponseDTO[], descending: boolean): PostResponseDTO[] {
    return posts.sort((a, b) => {
      const date1 = a.created_at ? new Date(a.created_at).getTime() : 0;
      const date2 = b.created_at ? new Date(b.created_at).getTime() : 0;
      return descending ? date2 - date1 : date1 - date2;
    });
  }
}