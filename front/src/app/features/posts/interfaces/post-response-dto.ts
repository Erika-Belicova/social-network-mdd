import { CommentResponseDTO } from "./comment-response-dto";

export interface PostResponseDTO {
  id: number;
  topic_id: number;
  topic_title: string;
  user_id: number;
  username: string;
  title: string;
  content: string;
  created_at: string;
  updated_at: string;
  comments: CommentResponseDTO[];
}