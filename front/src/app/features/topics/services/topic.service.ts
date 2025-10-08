import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TopicDTO } from '../interfaces/topic-dto';

@Injectable({
  providedIn: 'root'
})
export class TopicService {
  constructor(private http: HttpClient) {}

  getTopics(): Observable<TopicDTO[]> {
    return this.http.get<TopicDTO[]>('/api/topics');
  }

  getTopicById(id: number): Observable<TopicDTO> {
    return this.http.get<TopicDTO>(`/api/topics/${id}`);
  }
}
