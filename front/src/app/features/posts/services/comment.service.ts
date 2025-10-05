import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CommentRequestDTO } from '../interfaces/comment-request-dto';
import { CommentResponseDTO } from '../interfaces/comment-response-dto';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private baseUrl = '/api/posts';

  constructor(private http: HttpClient) {}

  addComment(postId: number, comment: CommentRequestDTO): Observable<CommentResponseDTO> {
    return this.http.post<CommentResponseDTO>(`${this.baseUrl}/${postId}/comments`, comment);
  }
}
