import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-post-form',
  templateUrl: './post-form.component.html',
  styleUrl: './post-form.component.scss'
})
export class PostFormComponent {
  topics = [];
  selectedTopic: number | null = null;

  constructor(private router: Router) { }

  submitPost() {
    // call service to submit post

    // get post id
    let postId = 1; // to test routing

    // navigate to post detail
    this.router.navigate(['/posts', postId]);
  }

}
