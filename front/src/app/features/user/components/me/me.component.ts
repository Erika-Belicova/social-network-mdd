import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { TopicService } from '../../../topics/services/topic.service';
import { TopicDTO } from '../../../topics/interfaces/topic-dto';
import { UserDTO } from '../../interfaces/user-dto';
import { UpdateUserDTO } from '../../interfaces/update-user-dto';

@Component({
  selector: 'app-me',
  templateUrl: './me.component.html',
  styleUrls: ['./me.component.scss']
})
export class MeComponent implements OnInit {
  profileForm!: FormGroup;
  currentUser!: UserDTO;
  userSubscriptions: TopicDTO[] = [];
  subscribedTopicIds = new Set<number>();

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private topicService: TopicService
  ) {}

  ngOnInit() {
    this.loadUserProfile();
  }

  loadUserProfile() {
    this.userService.getCurrentUser().subscribe({
      next: (user: UserDTO) => {
        this.currentUser = user;
        this.userSubscriptions = user.topics;
        this.subscribedTopicIds = new Set(user.topics.map(topic => topic.id));

        this.profileForm = this.fb.group({
          username: [user.username, Validators.required],
          email: [user.email, [Validators.required, Validators.email]],
          password: ['']
        });
      },
      error: (err) => console.error('Error loading user profile:', err)
    });
  }

  updateUser() {
    if (!this.profileForm.valid) return;

    const updatedData: UpdateUserDTO = {
      username: this.profileForm.value.username || this.currentUser.username,
      email: this.profileForm.value.email || this.currentUser.email,
      name: this.currentUser.username,
      password: this.profileForm.value.password
    };

    this.userService.updateUser(updatedData).subscribe({
      next: () => this.loadUserProfile(),
      error: (err) => console.error('Error updating user:', err)
    });
  }

  unsubscribe(topicId: number) {
    this.userService.unsubscribe(topicId).subscribe({
      next: () => {
        this.userSubscriptions = this.userSubscriptions.filter(topic => topic.id !== topicId);
        this.subscribedTopicIds.delete(topicId);
      },
      error: (err) => console.error('Error unsubscribing:', err)
    });
  }
}
