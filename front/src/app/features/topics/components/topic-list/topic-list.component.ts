import { Component, Input } from '@angular/core';
import { TopicDTO } from '../../../topics/interfaces/topic-dto';
import { UserService } from '../../../user/services/user.service';

@Component({
  selector: 'app-topic-list',
  templateUrl: './topic-list.component.html',
  styleUrls: ['./topic-list.component.scss']
})
export class TopicListComponent {
  @Input() topics: TopicDTO[] = [];
  @Input() subscribedTopicIds = new Set<number>();

  constructor(private userService: UserService) {}

  isSubscribed(topicId: number): boolean {
    return this.subscribedTopicIds.has(topicId);
  }

  subscribe(topicId: number) {
    this.userService.subscribe(topicId).subscribe({
      next: () => {
        this.subscribedTopicIds.add(topicId);
        console.log(`Subscribed to topic ${topicId}`);
      },
      error: (err) => console.error('Error subscribing:', err)
    });
  }
}
