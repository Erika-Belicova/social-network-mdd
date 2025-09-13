import { Component, OnInit } from '@angular/core';
import { TopicDTO } from '../../../topics/interfaces/topic-dto';
import { UserService } from '../../../user/services/user.service';
import { TopicService } from '../../../topics/services/topic.service';

@Component({
  selector: 'app-topic-list',
  templateUrl: './topic-list.component.html',
  styleUrls: ['./topic-list.component.scss']
})
export class TopicListComponent implements OnInit {
  topics: TopicDTO[] = [];
  subscribedTopicIds = new Set<number>();

  constructor(
    private userService: UserService,
    private topicService: TopicService
  ) {}

  ngOnInit(): void {
    // fetch current user subscriptions
    this.userService.getCurrentUser().subscribe({
      next: (user) => {
        this.subscribedTopicIds = new Set(user.topics.map(topic => topic.id));

        // then fetch topics
        this.topicService.getTopics().subscribe({
          next: (topics) => {
            this.topics = topics;
          },
          error: (err) => console.error('Error fetching topics:', err)
        });
      },
      error: (err) => console.error('Error fetching user subscriptions:', err)
    });
  }

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
