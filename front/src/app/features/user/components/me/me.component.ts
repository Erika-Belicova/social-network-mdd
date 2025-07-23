import { Component } from '@angular/core';

@Component({
  selector: 'app-me',
  templateUrl: './me.component.html',
  styleUrl: './me.component.scss'
})
export class MeComponent {

  updateUser() {
    // call service to update user information
  }

  unsubscribe(topicId: number) {
    // call service to unsubscribe user from the topic with the given topicId
  }

}
