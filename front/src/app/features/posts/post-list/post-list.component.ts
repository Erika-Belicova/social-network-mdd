import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-post-list',
  templateUrl: './post-list.component.html',
  styleUrl: './post-list.component.scss'
})
export class PostListComponent {
  posts = []; // posts will be fetched

  constructor(private router: Router) { }

  orderBy() {
    // order posts per specification
  }

  goToPost(postId: number) {
    // get id from the post clicked

    this.router.navigate(['/posts', postId]);
  }

}
