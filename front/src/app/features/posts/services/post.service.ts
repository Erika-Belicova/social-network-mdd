import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { PostRequestDTO } from '../interfaces/post-request-dto';
import { PostResponseDTO } from '../interfaces/post-response-dto';
import { PostsResponse } from '../interfaces/posts-response';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private baseUrl = '/api/posts';

  constructor(private http: HttpClient) {}

  createPost(postData: PostRequestDTO): Observable<PostResponseDTO> {
    return this.http.post<PostResponseDTO>(this.baseUrl, postData);
  }

  getPostById(id: number): Observable<PostResponseDTO> {
    return this.http.get<PostResponseDTO>(`${this.baseUrl}/${id}`);
  }

  getPosts(): Observable<PostResponseDTO[]> {
    return this.http.get<PostsResponse>(this.baseUrl).pipe(
      map(response => response.posts) // extract the posts array from API response
    );
  }
}
