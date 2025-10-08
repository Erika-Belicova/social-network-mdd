import { Component, OnInit } from '@angular/core';
import { TopicDTO } from '../../../topics/interfaces/topic-dto';
import { UserService } from '../../../user/services/user.service';
import { TopicService } from '../../../topics/services/topic.service';
import { SnackbarService } from '../../../../shared/services/snackbar.service';

@Component({
  selector: 'app-topic-list',
  templateUrl: './topic-list.component.html',
  styleUrls: ['./topic-list.component.scss']
})
export class TopicListComponent implements OnInit {
  topics: TopicDTO[] = [];
  subscribedTopicIds = new Set<number>(); // track subscribed topic IDs

  constructor(
    private userService: UserService,
    private topicService: TopicService,
    private snackbarService: SnackbarService
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
          error: () => {
            this.snackbarService.showError(
              "Impossible de récupérer les abonnements de l'utilisateur."
            );
          }
        });
      },
      error: () => {
        this.snackbarService.showError(
          "Impossible de récupérer les abonnements de l'utilisateur."
        );
      }
    });
  }

  isSubscribed(topicId: number): boolean {
    return this.subscribedTopicIds.has(topicId); // check if user is subscribed
  }

  subscribe(topicId: number) {
    // subscribe user to topic
    this.userService.subscribe(topicId).subscribe({
      next: () => {
        this.subscribedTopicIds.add(topicId);
      },
      error: () => {
        this.snackbarService.showError(
          "Impossible de s'abonner au thème sélectionné."
        );
      }
    });
  }
}
